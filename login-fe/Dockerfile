FROM nginx:alpine

LABEL maintainer="Luca Martinelli"

RUN rm -rf /usr/share/nginx/html/*

COPY nginx.conf /etc/nginx/nginx.conf
COPY /dist/login-fe /usr/share/nginx/html
COPY default.conf /etc/nginx/conf.d/default.conf

RUN chgrp -R root /var/cache/nginx /var/run /var/log/nginx && \
    chmod -R 777 /var/cache/nginx /var/run /var/log/nginx
RUN chgrp -R root /etc/nginx/conf.d /etc/nginx/ /usr/share/nginx/html && \
    chmod -R 777 /etc/nginx/conf.d /etc/nginx/ /usr/share/nginx/html

EXPOSE 8080

CMD ["nginx", "-g", "daemon off;"]