node {
  try {
    notifyBuild('STARTED')

    stage "Init"
      STAGE = "Init"
      checkout scm
      DEVELOP_BRANCH = "develop"
      MASTER_BRANCH = "master"
      PACKAGE_NAME_LOW = sh (script: "cat package.json | grep name | head -1 | awk -F: '{ print \$2 }' | sed 's/[\",]//g' | sed 's/ //g' | sed -e 's/./\\L\\0/g'", returnStdout: true).trim()
      PACKAGE_VERSION = sh (script: "cat package.json | grep version | head -1 | awk -F: '{ print \$2 }' | sed 's/ //g' | sed 's/[\",]//g'", returnStdout: true).trim()
      IMAGE_NAME = "${PACKAGE_NAME_LOW}-${PACKAGE_VERSION}"
      REGISTRY_HOST = "dev.sw-warehouse.xyz:1450"
      REGISTRY_USER = "root"
      REGISTRY_PASSWORD = "10WESfpwltmxmfl"
      SONAR_SCANNER_HOME = tool name: 'sonar-scanner', type: 'hudson.plugins.sonar.SonarRunnerInstallation'
      nodejs('nodejs10') {
        sh 'npm install'
      }
      if(env.BRANCH_NAME == DEVELOP_BRANCH) ENV_PHASE = "stage"
      else ENV_PHASE = "prd"

      echo "${PACKAGE_NAME_LOW}, ${PACKAGE_VERSION}, ${IMAGE_NAME}, ${REGISTRY_HOST}, ${REGISTRY_USER}, ${REGISTRY_PASSWORD}, ${ENV_PHASE}"

    if(env.BRANCH_NAME == DEVELOP_BRANCH){
			stage "Unit testing"
				STAGE = "Unit testing"
        nodejs('nodejs10') {
          sh 'npm test --ci --testResultsProcessor="jest-junit"'
        }
				junit "reports/junit.xml"

      stage "Test coverage"
				STAGE = "Unit testing"
        nodejs('nodejs10') {
          sh 'npm run test:coverage'
        }

      stage "Sonarqube"
				STAGE = "Sonarqube"
				withSonarQubeEnv("sonarqube") {
					sh "${SONAR_SCANNER_HOME}/bin/sonar-scanner"
				}
				sleep 30
				timeout(time: 1, unit: "MINUTES") {
					def qg = waitForQualityGate()
						if (qg.status != "OK") {
							error "Pipeline aborted due to quality gate failure: ${qg.status}"
						}
				}

      stage "Publish image"
        STAGE = "Publish image"
        nodejs('nodejs10') {
          sh 'npm run build'
        }
				withDockerRegistry([credentialsId: 'registry', url: 'https://dev.sw-warehouse.xyz:1450']) {
					def image = docker.build("$REGISTRY_HOST/$IMAGE_NAME")
					image.push()
				}

			stage "Deploy on stage"
				STAGE = "Deploy on stage"
				sh "sshpass -p '0)8*WESehzj' ssh -T -oStrictHostKeyChecking=no -p 22000 docker@dev.sw-warehouse.xyz \"docker rm -f ${IMAGE_NAME}-${ENV_PHASE} 2> /dev/null | echo ok && docker login -u ${REGISTRY_USER} -p ${REGISTRY_PASSWORD} ${REGISTRY_HOST} && docker pull ${REGISTRY_HOST}/${IMAGE_NAME} && docker run -d --network=tmpdir-${ENV_PHASE}-net -p 6000:6000 -e ENV_PHASE='${ENV_PHASE}' -v /app/tmpdir-fileupload-${ENV_PHASE}/config:/app/build/config -v /etc/letsencrypt:/app/certs -v /applog/tmpdir-fileupload-${ENV_PHASE}:/applog -v /db/tmpdir-${ENV_PHASE}/storage:/storage --name ${IMAGE_NAME}-${ENV_PHASE} ${REGISTRY_HOST}/${IMAGE_NAME}\""
    }

    if(env.BRANCH_NAME == MASTER_BRANCH){
			stage "Deploy on product"
				STAGE = "Deploy on product"
				sh "sshpass -p '0)8*WESehzj' ssh -T -oStrictHostKeyChecking=no -p 22000 docker@dev.sw-warehouse.xyz \"docker rm -f ${IMAGE_NAME}-${ENV_PHASE} 2> /dev/null | echo ok && docker login -u ${REGISTRY_USER} -p ${REGISTRY_PASSWORD} ${REGISTRY_HOST} && docker pull ${REGISTRY_HOST}/${IMAGE_NAME} && docker run -d --network=tmpdir-${ENV_PHASE}-net -e ENV_PHASE='${ENV_PHASE}' -v /app/tmpdir-fileupload-${ENV_PHASE}/config:/app/build/config -v /etc/letsencrypt:/app/certs -v /applog/tmpdir-fileupload-${ENV_PHASE}:/applog -v /db/tmpdir-${ENV_PHASE}/storage:/storage --name ${IMAGE_NAME}-${ENV_PHASE} ${REGISTRY_HOST}/${IMAGE_NAME}\""
		}

  } catch (e) {
    currentBuild.result = "FAILED"
      throw e
  } finally {
    notifyBuild(currentBuild.result, STAGE)
  }
}

def notifyBuild(String buildStatus = 'STARTED', String stage = 'NONE') {
  buildStatus =  buildStatus ?: 'SUCCESSFUL'

  def colorName = 'RED'
  def colorCode = '#FF0000'
  def subject = "Job: ${env.JOB_NAME}, Stage: ${stage} [${env.BUILD_NUMBER}] = ${buildStatus}"
  if(stage != '' && buildStatus == 'FAILED') subject += ", Stage '${stage}'"
  def summary = "${subject} (${env.BUILD_URL})"

  if (buildStatus == 'STARTED') {
    color = 'YELLOW'
      colorCode = '#FFFF00'
  } else if (buildStatus == 'SUCCESSFUL') {
    color = 'GREEN'
      colorCode = '#00FF00'
  } else {
    color = 'RED'
      colorCode = '#FF0000'
  }

  slackSend (color: colorCode, message: summary)
}
