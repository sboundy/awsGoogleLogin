AWS Google Login
=================

A lightweight application to broker the Google login and subsequent calls to AWS's STS service

This application is very much a proof of concept application but one that can be trimmed down further.  It can be run locally or remotely.

API calls
----------

This application makes a number of API calls and receives call back from Google containing web token details.  This web token is used to call Amazon's Simple Token Service and request a short lived secret, access and session key.  A short lived console login is also constructed allowing for login without credentials.

TODO

Installation
-------------

This is a simple play java application.  It requires both an AWS account and access to the Google developers console to setup a new application