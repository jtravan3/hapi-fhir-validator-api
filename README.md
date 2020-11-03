# HAPI FHIR Validator API

## What is the FHIR Validator API?

The HAPI FHIR Validator API is a simple REST API in order to validate the structure and content of a FHIR object. The API uses
the HAPI FHIR framework located at [https://hapifhir.io/](https://hapifhir.io/) in order to validate different all versions
of FHIR objects. [FHIR](https://www.hl7.org/fhir/overview.html) stands for Fast Healthcare Interoperability Resources which
is a standard for data transmission within healthcare.

## Structure of the API

See our [Swagger technical reference](./docs/technical-reference)

## Get Started

Check out our [How To Validate FHIR Resources Guide](./docs/how-to-validate-fhir-resources)

## Developer Setup

### Environment Setup (Without Docker)

1.) Install maven 3.6.0 and Java JDK 11. JDK located [here](https://openjdk.java.net/install/) or use the Maven wrapper
that's a part of this repository.

```bash
brew install maven
```

2.) Clone down the repository from Github

```bash
git clone git@github.com:jtravan3/hapi-fhir-validator-api.git
```

3.) After populating the properties build the project

```bash
./mvnw clean install
```

4.) Run the api.

```bash
./mvnw spring-boot:run
```

The project is configured with Spring dev tools which allows for hot reloads without restarting the application. Simply
rebuild the project after making a change (In IntelliJ use `Build -> Build Project` or `CMD + F9`) and the application will
auto re-deploy.

5.) Access the API by visiting `http://localhost:5000/swagger-ui.html`. From there you can hit the endpoints directly.

6.) Success!

## Docker Setup

1.) Install Docker located [here](https://docs.docker.com/docker-for-mac/install/).

2.) Clone down the repository from Github

```bash
git clone git@github.com:jtravan3/hapi-fhir-validator-api.git
```

3.) From the root directory run the following command to build the project.

```bash
docker-compose build
```

4.) To run the API

```bash
docker-compose up
```

5.) Access the API by visiting `http://localhost:5000/swagger-ui.html`. From there you can hit the endpoints directly.

6.) Success!

## Using Swagger

1.) Access the API at `http://localhost:5000/swagger-ui.html`. You should see the following homepage.

![Swagger Home Page](./images/swagger-home.png)

2.) Once on the API, select the validation endpoint that you want to try out.

![Validation Endpoint](./images/validation-endpoint.png)

3.) Replace the `{}` with the FHIR JSON object that you want to validate

![FHIR JSON String](./images/fhir-json-string.png)

4.) Click *Execute* and then scroll down to see the response. If the response is successful you should see a JSON response of success

```json
{
  "successful": true,
  "errors": [],
  "warnings": [],
  "info": [],
  "exception": null
}
```

If there are issues the messages will be associated with their designated severity level.

```json
{
  "successful": false,
  "errors": [
    {
      "message": "Unrecognised property '@nae'",
      "locationString": "/Parameters/parameter",
      "severity": "ERROR",
      "locationLine": null,
      "locationCol": null
    },
    {
      "message": "Unrecognised property '@valeString'",
      "locationString": "/Parameters/parameter/part",
      "severity": "ERROR",
      "locationLine": null,
      "locationCol": null
    },
    {
      "message": "Profile http://hl7.org/fhir/StructureDefinition/Parameters, Element 'Parameters.parameter[1].name': minimum required = 1, but only found 0",
      "locationString": "Parameters.parameter[1]",
      "severity": "ERROR",
      "locationLine": null,
      "locationCol": null
    },
    {
      "message": "cvc-complex-type.2.4.a: Invalid content was found starting with element '{\"http://hl7.org/fhir\":valueBoolean}'. One of '{\"http://hl7.org/fhir\":extension, \"http://hl7.org/fhir\":modifierExtension, \"http://hl7.org/fhir\":name}' is expected.",
      "locationString": null,
      "severity": "ERROR",
      "locationLine": 1,
      "locationCol": 79
    }
  ],
  "warnings": [],
  "info": [],
  "exception": null
}
```

