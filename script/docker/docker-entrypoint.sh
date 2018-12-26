#!/bin/bash
set -e

echo "NPM Install Production"
cd /app/ && npm install --production

exec "$@"
