pipeline {
    agent any

    environment {
        DOCKER_COMPOSE_FILE = 'docker-compose.yml'
        APP_NAME = 'shortlink'
    }

    stages {
        stage('Build') {
            steps {
                echo 'mvn package...'
                sh 'mvn -B -DskipTests clean package'
                echo 'docker building....'
                sh 'docker compose -f $DOCKER_COMPOSE_FILE build'
            }
        }

        stage('Deploy') {
            steps {
                echo 'Deploying...'

                // 停止现有服务（如果运行中）
                sh 'docker compose -f $DOCKER_COMPOSE_FILE down || true'

                // 启动服务（使用 detached 模式）
                sh 'docker compose -f $DOCKER_COMPOSE_FILE up -d'

                // 验证服务是否启动成功
                sh 'sleep 60' // 等待服务启动
                sh 'curl -s http://localhost:8080/actuator/health || exit 1'
                sh 'curl -s http://localhost:8081/actuator/health || exit 1'
                sh 'curl -s http://localhost:8082/actuator/health || exit 1'
            }
        }

        stage('Post-Deployment Checks') {
            steps {
                echo 'Performing post-deployment checks...'
                // 检查容器状态
                sh 'docker ps -a'
                // 查看日志
                sh 'docker compose -f $DOCKER_COMPOSE_FILE logs'
            }
        }
    }
}