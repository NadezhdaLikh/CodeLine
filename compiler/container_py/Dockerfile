FROM python:3.9-slim

WORKDIR /app

USER root

ADD . .

# chmod (от англ. change mode) — команда для изменения прав доступа к файлам
# и каталогам, используемая в Unix-подобных операционных системах

RUN chmod a+x ./main.py
RUN chmod a+x ./entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]
