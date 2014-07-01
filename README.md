AWS Google Login
=================

A lightweight application to allow Google auth to be used with the [AWS STS service](http://docs.aws.amazon.com/STS/latest/APIReference/Welcome.html) via [IAM federation](http://aws.amazon.com/blogs/aws/aws-iam-now-supports-amazon-facebook-and-google-identity-federation/).

While federation of Google auth is usually used by applications this small application allows it to be used for CLI or console access. This application is very much a proof of concept application but one that can be trimmed down further.  It can be run locally or remotely.

API calls
----------

This application makes a number of API calls and receives call back from Google containing web token details.  This web token is used to call Amazon's Simple Token Service and request a short lived secret, access and session key.  A short lived console login is also constructed allowing for login without credentials.

TODO

Installation
-------------

This is a simple play java application.  It requires both an AWS account and access to the Google developers console to setup a new application