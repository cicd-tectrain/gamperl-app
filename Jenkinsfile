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
            }
        }

        stage("Test Feature"){
            steps{
                echo "Testing..."
            }
        }

        stage("Integrate Feature"){
            steps{
                echo "Integrating..."
            }
        }
    }
}