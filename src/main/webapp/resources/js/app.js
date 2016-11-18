'use strict';

// Declare app level module which depends on filters, and services
var app = angular.module('distributeApp', ['ngRoute', 'smart-table', 'angularFileUpload', 'ui.bootstrap', 'angular-loading-bar'
]);

app.config(['$routeProvider', '$locationProvider', function AppConfig($routeProvider, $locationProvider) {
	$routeProvider.

	when('/', {
		redirectTo : '/projects',
	}).

	when('/home', {
		templateUrl : 'resources/partials/home/home.html'
	}).

	when('/upload', {
		templateUrl : 'resources/partials/uploadView/upload.html'
	}).

	when('/download/site', {
		templateUrl : 'resources/partials/downloadView/site.html'
	}).

	when('/download/core', {
		templateUrl : 'resources/partials/downloadView/core.html'
	}).
	
	when('/download/:category', {
		templateUrl : 'resources/partials/download/download.html'
	}).

	when('/projects', {
		templateUrl : 'resources/partials/project/projects.html'
	}).

	when('/historyDownload', {
		templateUrl : 'resources/partials/historyView/historyDownload.html'
	}).

	when('/historyUpgrade', {
		templateUrl : 'resources/partials/historyView/historyUpgrade.html'
	}).

	when('/users', {
		templateUrl : 'resources/partials/user/users.html'
	}).

	when('/user/:id', {
		templateUrl : 'resources/partials/user/user.html'
	}).

	when('/project/:id', {
		templateUrl : 'resources/partials/project/project.html'
	}).

	when('/password', {
		templateUrl : 'resources/partials/user/password.html'
	}).
	
	when('/error', {
		templateUrl : 'resources/partials/home/error.html'
	}).

	otherwise({
		redirectTo : '/error'
	});
}
]);

app.config(function(cfpLoadingBarProvider) {
	cfpLoadingBarProvider.includeSpinner = true;
})
