# Read Me

### About
Spring boot 2.5.2 rest application.  
Firebase backend.  
Hosted on heroku.  
Spring-fox used to create swagger api-spec.  


### Running locally
Requires the serviceAccountKey.json file to be stored in root directory.  
(This value is not available in github, and is stored as a config variable in heroku.)  

### Heroku url's
[Find a Product by SKU](https://products-service-alpha.herokuapp.com/find/product/12345)  
[Find products on a condition](https://products-service-alpha.herokuapp.com/find/products?field=name&value=samsung&operation=equal)  
[API Spec](https://products-service-alpha.herokuapp.com/swagger-ui.html)  

### Build
[Build through Circle CI](https://app.circleci.com/pipelines/github/soneill1)  