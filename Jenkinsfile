pipeline {
    agent any 

    tools {
        jdk 'Java_17' // Gọi tên JDK 17 bạn cài ở giao diện
        maven 'Maven_3'
    }

    stages {
        stage('Kiểm tra phiên bản') {
            steps {
                sh 'java -version'
                sh 'mvn -version'
            }
        }

        // ========================================================
        // GIAI ĐOẠN 1: BUILD DỰ ÁN SECURITY SERVICE
        // ========================================================
        stage('Build & Đóng gói Security Service') {
            steps {
                echo '--- Đang tiến hành build Security Service ---'
                dir('source/01.backend/securityService') {
                    sh 'mvn clean package -DskipTests -Dfile.encoding=UTF-8'
                }
            }
        }

        stage('Kiểm tra sản phẩm Security Service') {
            steps {
                dir('source/01.backend/securityService') {
                    echo 'Các file .jar của Security Service:'
                    sh 'ls -la target/'
                }
            }
        }

        // ========================================================
        // GIAI ĐOẠN 2: BUILD DỰ ÁN CATEGORY MANAGEMENT
        // ========================================================
        stage('Build & Đóng gói Category Management') {
            steps {
                echo '--- Đang tiến hành build Category Management ---'
                // Trỏ sang đường dẫn của dự án thứ hai
                dir('source/01.backend/categoryManagement') {
                    sh 'mvn clean package -DskipTests -Dfile.encoding=UTF-8'
                }
            }
        }

        stage('Kiểm tra sản phẩm Category Management') {
            steps {
                dir('source/01.backend/categoryManagement') {
                    echo 'Các file .jar của Category Management:'
                    sh 'ls -la target/'
                }
            }
        }

        // ========================================================
        // GIAI ĐOẠN 3: BUILD DỰ ÁN USER MANAGEMENT
        // ========================================================
        stage('Build & Đóng gói User Management') {
            steps {
                echo '--- Đang tiến hành build User Management ---'
                // Trỏ sang đường dẫn của dự án thứ hai
                dir('source/01.backend/userManagement') {
                    sh 'mvn clean package -DskipTests -Dfile.encoding=UTF-8'
                }
            }
        }

        stage('Kiểm tra sản phẩm user Management') {
            steps {
                dir('source/01.backend/userManagement') {
                    echo 'Các file .jar của User Management:'
                    sh 'ls -la target/'
                }
            }
        }
    }
}