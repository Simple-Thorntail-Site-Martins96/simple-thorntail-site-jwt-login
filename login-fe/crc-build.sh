npm i
ng build --prod --aot
docker build --tag default-route-openshift-image-registry.apps-crc.testing/simple-site/login-fe:latest .
docker push default-route-openshift-image-registry.apps-crc.testing/simple-site/login-fe
oc new-app --docker-image=image-registry.openshift-image-registry.svc:5000/simple-site/login-fe --name=login-fe
oc expose svc/login-fe --port=8080
