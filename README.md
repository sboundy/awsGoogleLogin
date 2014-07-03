AWS Google Login
=================

A lightweight application to allow Google auth to be used with the [AWS STS service](http://docs.aws.amazon.com/STS/latest/APIReference/Welcome.html) via [IAM federation](http://aws.amazon.com/blogs/aws/aws-iam-now-supports-amazon-facebook-and-google-identity-federation/).

Federation of Google auth is often used where client side access is needed to AWS resource (e.g. in mobile applications).  This application, however, allows Google auth to be used to issue tokens for CLI or console access. This application is very much a proof of concept application but one that can be trimmed down further.  It can be run locally or remotely.

Sequence of API calls
----------------------

This application makes the following API calls

- A number of calls via the [Google OAuth flow](https://developers.google.com/accounts/docs/OAuth2#basicsteps)
- A call to STS to retrieve a short lived secret, access and session key
- A call to the AWS sign in federation service to retrieve a short-lived console url

In addition, the application will update credentials in the .aws/config file.

