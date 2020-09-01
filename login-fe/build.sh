npm i
ng build --prod --aot
docker build --tag 172.30.1.1:5000/simple-site-project/login-fe:latest .
docker push 172.30.1.1:5000/simple-site-project/login-fe
oc new-app --docker-image=172.30.1.1:5000/simple-site-project/login-fe --name=login-fe
oc expose svc/login-fe --port=8080