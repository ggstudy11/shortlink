pipeline {
    agent any
    stages {
        stage ('Build') {
            steps {
                echo 'mvn -B -DskipTests clean package'
                sh 'docker compose build'
            }
        }
    }
}