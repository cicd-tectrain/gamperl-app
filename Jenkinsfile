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
    }
}