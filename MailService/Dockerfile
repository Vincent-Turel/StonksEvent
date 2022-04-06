FROM mono:6.12

LABEL org.opencontainers.image.authors="StonksDev surname.name@univ-cotedazur.fr"

# Loading the executable server inside the image
COPY ./MailService/bin/Debug/net6.0/MailService.dll ./MailService.dll

# exposing the 9090 port to support external connections
EXPOSE 9090

# Running the .Net server as standalone
ENTRYPOINT ["mono", "./MailService.dll"]
CMD ["/standalone"]