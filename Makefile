images: backend-image cli-image mail-service-image
publish: backend-publish cli-publish mail-service-publish


# Building java stuff
backend-jar:
	mvn clean package -f backend -DskipTests

cli-jar:
	mvn clean package -f cli -DskipTests


# Building images
backend-image: backend-jar
	docker build --build-arg JAR_FILE=event-app/target/event-app-0.0.1-SNAPSHOT.jar -t scrabsha/spring-backend backend/.
	# docker build --build-arg JAR_FILE=event-app/target/event-app-0.0.1-SNAPSHOT.jar -t stonksevent/spring-backend backend/.
	# docker tag stonksevent/mail-service scrabsha/mail-service

cli-image: cli-jar
	docker build --build-arg JAR_FILE=target/cli-0.0.1-SNAPSHOT.jar -t scrabsha/spring-cli cli/.
	# docker build --build-arg JAR_FILE=target/cli-0.0.1-SNAPSHOT.jar -t stonksevent/spring-cli cli/.
	# docker tag stonksevent/mail-service scrabsha/mail-service

mail-service-image:
	docker build -t scrabsha/mail-service MailService/.
	# docker build -t stonksevent/mail-service MailService/.
	# docker tag stonksevent/mail-service scrabsha/mail-service

# Publishing images
backend-publish: backend-image
	docker push scrabsha/spring-backend

cli-publish: cli-image
	docker push scrabsha/spring-cli

mail-service-publish: mail-service-image
	docker push scrabsha/mail-service


launch: backend-image cli-image mail-service-image
	docker-compose up -d

stop:
	docker-compose down
	
