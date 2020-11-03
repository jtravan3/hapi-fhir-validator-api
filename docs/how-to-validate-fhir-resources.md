# How To Validate FHIR Resources

This guide describes how an application can validate a FHIR object using the API

## Javascript

To validate a FHIR object, first determine which version of FHIR you wan to validate against. There are four API endpoints
for each endpoint that is possible to validate against. They are:

* `POST /fhir/r4/validate`
* `POST /fhir/v2/validate`
* `POST /fhir/v2_1/validate`
* `POST /fhir/v3/validate`

In this example we'll use the `r4` endpoint.

We will be using `isomorphic-fetch` from npm, go ahead and `npm install --save isomorphic-fetch` or use your favorite request library.

### Successful Validation

Below is an example of a valid FHIR object

```js
const fetch = require('isomorphic-fetch')

const requestBody = {
  resourceType: 'Parameters',
  parameter: [
    {
      name: 'result',
      valueBoolean: true
    },
    {
      name: 'innerPackageSize',
      valueDecimal: 6.7
    },
    {
      name: 'outerPackageSize',
      valueDecimal: 1
    },
    {
      name: 'packageUnitCode',
      valueString: '3'
    },
    {
      name: 'packageUnitDesc',
      valueString: 'Grams'
    }
  ]
}

const validateR4Fhir = async () => {
  return fetch('localhost:5000/fhir/r4/validate', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/fhir+json',
      Accept: 'application/fhir+json'
    },
    body: JSON.stringify(requestBody)
  })
}

validateR4Fhir()
  .then(res => res.json())
  .then(res => {
    console.log(res.body)
  })
```

### Successful Response

With the code above you should see the following response.

```json
{
    "successful": true,
    "errors": [],
    "warnings": [],
    "info": [],
    "exception": null
}
```

### Failed Validation

Here is an example of a failed validation due to the `valueBoolean` attribute being considered a String

```js
const fetch = require('isomorphic-fetch')

const requestBody = {
  resourceType: 'Parameters',
  parameter: [
    {
      name: 'result',
      valueBoolean: 'true'
    },
    {
      name: 'innerPackageSize',
      valueDecimal: 6.7
    },
    {
      name: 'outerPackageSize',
      valueDecimal: 1
    },
    {
      name: 'packageUnitCode',
      valueString: '3'
    },
    {
      name: 'packageUnitDesc',
      valueString: 'Grams'
    }
  ]
}

const validateR4Fhir = async () => {
  return fetch('localhost:5000/fhir/r4/validate', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/fhir+json',
      Accept: 'application/fhir+json'
    },
    body: JSON.stringify(requestBody)
  })
}

validateR4Fhir()
  .then(res => res.json())
  .then(res => {
    console.log(res.body)
  })
```

### Failed Response

With the code above you should see the following response.

```json
{
    "successful": false,
    "errors": [
        {
            "message": "Error parsing JSON: the primitive value must be a boolean",
            "severity": "ERROR",
            "locationCol": null,
            "locationLine": null,
            "locationString": "/Parameters/parameter/value[x]"
        }
    ],
    "warnings": [],
    "info": [],
    "exception": null
}
```

## Response Analysis

Each response has a top-level `successful` attribute that determines if the overall validation was successful. Other attributes
include `errors`, `warnings`, and `info` which are all arrays that will include more detailed information if available. The final
attribute is `exception` which will be available if the validation was not able to run due to a runtime error.

Fight FHIR with FHIR!
