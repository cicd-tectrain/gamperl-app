pipeline {

    agent any

    stages {
        stage("Build Feature"){
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