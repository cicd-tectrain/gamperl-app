pipeline {

    agent any

    stages {
        stage("Build Feature"){

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
            }
        }

        stage("Integrate Feature"){
            steps{
                echo "Integrating..."
            }
        }
    }
}