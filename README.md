# Vert.x HTTP Event Bus Bridge


## What is Vert.x HTTP Event Bus Bridge

The Vert.x event bus bridge allows non-Vert.x-based clients to utilise the Vert.x event bus over a HTTP REST interface.

It currently supports the following:

* Send and publish messages to an event bus
* Asynchronously send responses back to the client who made the request
* The ability to restrict the addresses which messages are sent or published to


## How To Run

* First build and install the module to your local repository using Maven (if building from source):

	`mvn clean install`
  
* Next, set up the Vert.x arguments to use the HK2 binder (required due to dependency on vertx-mod-jersey)

	Windows:

	`set VERTX_OPTS=-Dvertx.langs.java=com.englishtown~vertx-mod-hk2~1.7.0:com.englishtown.vertx.hk2.HK2VerticleFactory`
  	
	Linux/Unix:

	`export VERTX_OPTS=-Dvertx.langs.java=com.englishtown~vertx-mod-hk2~1.7.0:com.englishtown.vertx.hk2.HK2VerticleFactory`

* Now run the module:

	`vertx runMod io.vertx~http-event-bus-bridge~<version> -conf <config-file> -cluster`


## Configuration

Configuration is managed through a single configuration file, and specified on the command line when executing the module. Below is an example configuration file:

```json
{
    "base_path": "/",
    "resources": ["org.vertx.java.http.eventbusbridge.service"],
    "features": ["org.glassfish.jersey.jackson.JacksonFeature"],
    "host": "localhost",
    "port": 8080,
    "timeout": 60000,
    "whitelist": {
    	"inbound": {
    		"address": ["testaddress"],
    		"address_re": ["valid.*"]
    	}
    }
}
```

The fields in this configuration file are as follows:

* `base_path` - The base URL path
* `resources` -  Jersey resources. Do not change this value.
* `features` - Jersey features. Do not change this value.
* `host` - The hostname of the server 
* `port` - The port to listen to HTTP requests on
* `timeout` - The response handler timeout period 
* `whitelist` - List of whitelisted event bus addresses
  - `inbound` - Inbound whitelisted event bus addresses
      - `address` - Whitelisted address
      - `address_re` - Whitelisted address regex

## HTTP API

All HTTP API requests made to the HTTP Event Bus Bridge need to include a Content-Type header to indicate of the mime type of the request. The supported values are `application/json` (for json) and `application/xml` (for xml).

### Send Request

Send requests utilise the Vert.x API call: `eb.send(address, message)`. These requests are HTTP POST requests, and are sent to the URL: `http://<hostname>:<port>/eventbus/send`.


##### Example XML

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<eventBusBridgeRequest>
    <address><!-- address --></address>
    <messageType><!-- messageType --></messageType>
    <message><!-- message --></message>
</eventBusBridgeRequest>
```

##### Example JSON

```json
{
  "address" : "<address>",
  "message" : "<message>",
  "messageType" : "<messageType>"
}
```


### Send With Reply Request

Send requests with replies utilise the Vert.x API call `eb.send(address, message, handler)`. These requests are HTTP POST requests, and 
are sent to the URL: `http://<hostname>:<port>/eventbus/send`. Responses are sent asynchronously to the URL specified in 'responseUrl' field of the request.

##### Example XML

###### Request

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<eventBusBridgeRequest>
    <address><!-- address --></address>
    <messageType><!-- messageType --></messageType>
    <message><!-- message --></message>
    <responseMediaType><!-- responseMediaType --></responseMediaType>
    <responseUrl><!-- responseUrl --></responseUrl>
</eventBusBridgeRequest>
```

###### Response

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<eventBusBridgeResponse>
    <address><!-- address --></address>
    <messageType><!-- messageType --></messageType>
    <message><!-- message --></message>
    <successful><!-- successful --></successful>
    <cause><!-- cause --></cause>
</eventBusBridgeResponse>
```

##### Example JSON

###### Request

```json
{
  "address" : "<address>",
  "message" : "<message>",
  "messageType" : "<messageType>",
  "responseMediaType" : "<responseMediaType>",
  "responseUrl" : "<responseUrl>"
}
```

###### Response

```json
{
  "address" : "<address>",
  "successful" : <successful>,
  "message" : "<message>",
  "messageType" : "<messageType>"
  "cause" : "<cause>"
}
```

### Publish Request

Publish requests utilise the Vert.x API call `eb.publish(address, message)`. These requests are HTTP POST requests, and are sent to the URL: `http://<hostname>:<port>/eventbus/publish`.

##### Example XML

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<eventBusBridgeRequest>
    <address><!-- address --></address>
    <messageType><!-- messageType --></messageType>
    <message><!-- message --></message>
</eventBusBridgeRequest>
```


##### Example JSON

```json
{
  "address" : "<address>",
  "message" : "<message>",
  "messageType" : "<messageType>"
}
```

 

Below is a short description of each of the fields used in this API:

* `address` - The event bus address that the message is destined for
* `message` -  The Base64 encoded representation of the message.
* `messageType` - The message type ('String', 'Boolean', 'Byte', 'ByteArray', 'Character', 'Double', 'Float', 'Integer', 'JsonArray', 'JsonObject', 'Long' or 'Short').
* `responseMediaType` - The MIME type of the expected response ('application/xml' or 'application/json') 
* `responseUrl` - Path that the response will be POSTed to
* `successful` - Whether or not the response was successfully sent
* `cause` - If the response was not received successfully, the reason why (e.g. no handlers registered for address).



