node {
  try {
    notifyBuild('STARTED')

    stage "Init"
      STAGE = "Init"
      checkout scm
      DEVELOP_BRANCH = "nodejs"
      MASTER_BRANCH = "master"
      PACKAGE_VERSION = sh script: "cat package.json | grep name | head -1 | awk -F: '{ print \$2 }' | sed 's/[\",]//g'", returnStdout: true
      PACKAGE_NAME_LOW = sh script: "cat package.json | grep name | head -1 | awk -F: '{ print \$2 }' | sed 's/[\",]//g' | sed -e 's/./\\L\\0/g'", returnStdout: true
      PACKAGE_VERSION = sh script: "cat package.json | grep version | head -1 | awk -F: '{ print \$2 }' | sed 's/[\",]//g'", returnStdout: true
      IMAGE_NAME = "tmpdir-${PACKAGE_NAME_LOW}-${PACKAGE_VERSION}"
      REGISTRY_HOST = "dev.sw-warehouse.xyz:1450"
      REGISTRY_USER = "root"
      REGISTRY_PASSWORD = "10WESfpwltmxmfl"
      SONAR_SCANNER_HOME = tool name: 'sonar-scanner', type: 'hudson.plugins.sonar.SonarRunnerInstallation'
      nodejs('nodejs10') {
        sh 'npm install'
      }
      if(env.BRANCH_NAME == DEVELOP_BRANCH) ENV_PHASE = "stage"
      else ENV_PHASE = "prd"

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
    }

  } catch (e) {
    currentBuild.result = "FAILED"
      throw e
  } finally {
    notifyBuild(currentBuild.result, STAGE)
  }
}

def notifyBuild(String buildStatus = 'STARTED', String stage = '') {
  buildStatus =  buildStatus ?: 'SUCCESSFUL'

  def colorName = 'RED'
  def colorCode = '#FF0000'
  def subject = "${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'"
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
