pipeline {

    agent any

    environment {
        INTEGRATION_BRANCH = 'integration'
    }

    stages {
        stage("Build Feature"){
            // Limit Branches
            when {
                branch 'feature/*'
                beforeAgent true
            }
            // Docker Agent
            agent {
              docker {
                image 'gradle:7.5.1-jdk17-focal'
              }
            }

            steps{
                echo "Building..."
                sh 'gradle clean build -x test'
                sh 'ls -la build/libs'
            }
        }

        stage("Test Feature"){
            // Limit Branches
            when {
                branch 'feature/*'
                beforeAgent true
            }
            // Docker Agent
            agent {
              docker {
                image 'gradle:7.5.1-jdk17-focal'
              }
            }

            steps{
                echo "Testing..."
                sh 'gradle test'
                // JUNit XML Reports
                sh 'ls -la build/test-results/test'
                sh 'ls -la build/reports/tests'
            }

            // Post-Build Actions
            post {
                always {
                    // JUnit Results archivieren
                    junit 'build/test-results/test/*.xml'
                }

                success {
                    publishHTML target: [
                        allowMissing: true,
                        alwaysLinkToLastBuild: false,
                        keepAll: true,
                        reportDir: 'build/reports/tests/test',
                        reportFiles: 'index.html',
                        reportName: 'Test-Report'
                    ]
                }

            }
        }

        stage("Integrate Feature"){
            // Limit Branches
            when {
                branch 'feature/*'
                beforeAgent true
            }

            // Hier wieder agent any
            steps{
                echo "Integrating..."
                sh 'git --version'
                sh 'git branch -a'
                sh 'git checkout ${INTEGRATION_BRANCH}'
                sh 'git pull --ff-only'
                // FIX ME
                sh 'git merge --no-ff --no-edit remotes/origin/${BRANCH_NAME}'

                // Pushen
                withCredentials([gitUsernamePassword(credentialsId: 'github_pat', gitToolName: 'Default')]) {
                    sh 'git push origin ${INTEGRATION_BRANCH}'
                }
            }
        }

        // ========================= INTEGRATION =========================
        stage("Build Integration"){
            // Limit Branches
            when {
                branch "${INTEGRATION_BRANCH}"
                beforeAgent true
            }
            // Docker Agent
            agent {
              docker {
                image 'gradle:7.5.1-jdk17-focal'
              }
            }

            steps{
                echo "Building..."
                sh 'gradle clean build -x test'
                sh 'ls -la build/libs'
            }

            // Stash if stage was successful
            post {
                success{
                    stash name: 'integration_build', includes: 'build/'
                }
            }
        }

        stage("Test Integration"){
            // Limit Branches
            when {
                branch "${INTEGRATION_BRANCH}"
                beforeAgent true
            }
            // Docker Agent
            agent {
              docker {
                image 'gradle:7.5.1-jdk17-focal'
              }
            }

            steps{
                echo "Testing..."
                sh 'gradle test'
                // JUNit XML Reports
                sh 'ls -la build/test-results/test'
                sh 'ls -la build/reports/tests'
            }

            // Post-Build Actions
            post {
                always {
                    // JUnit Results archivieren
                    junit 'build/test-results/test/*.xml'
                }

                success {
                    publishHTML target: [
                        allowMissing: true,
                        alwaysLinkToLastBuild: false,
                        keepAll: true,
                        reportDir: 'build/reports/tests/test',
                        reportFiles: 'index.html',
                        reportName: 'Test-Report'
                    ]
                }
            }
        }

        stage("Publish Artifacts") {
             // Limit Branches
             when {
                 branch "${INTEGRATION_BRANCH}"
                 beforeAgent true
             }

            steps {
                // Unstash
                unstash 'integration_build'

                // Publish Artifact in Nexus
                nexusArtifactUploader artifacts: [
                [
                    artifactId: at.tectrain.app,
                    classifier: '',
                    file: 'build/libs/app-0.0.1-SNAPSHOT.jar',
                    type: 'jar'
                ]],
                credentialsId: 'nexus_credentials',
                nexusVersion: 'nexus3'
                groupId: '',
                nexusUrl: 'nexus:8081/repository/maven-snapshots',
                protocol: 'http',
                repository: '',
                version: '0.0.1-SNAPSHOT'
            }
        }

        stage('Deploy Integration branch') {
            when {
                branch "${INTEGRATION_BRANCH}"
                beforeAgent true
            }

            // Docker image bauen und starten (und archivieren)

            // Env für Nexus Credentials
            environment {
                NEXUS_CREDENTIALS = credentials('nexus_credentials')
            }
            steps {
                unstash 'integration_build'

                // Image bauen -> Dockerfile
                sh 'docker build -t nexus:5000/app:latest -f docker/integration/Dockerfile .'
                // Image taggen

                sh 'echo ${NEXUS_CREDENTIALS_PSW} | docker login -u ${NEXUS_CREDENTIALS_USR} --password-stdin nexus:5000'

                // Image pushen
                sh 'docker push nexus:5000/app:latest'

                sh 'docker container run -p 8090:8085 --name testing -d --rm app:latest'
            }

            post {
                always {
                    sh 'docker logout nexus:5000'
                }
            }
        }
    }
}