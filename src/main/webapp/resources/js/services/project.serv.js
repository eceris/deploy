'use strict';

/* Services */

app.factory('projectFactory', function($q, $filter, $timeout, $http, cfpLoadingBar) {
	var projectFactory = {};
	projectFactory.getProjectList = function(start, number, params) {
		console.log(params);
		var deferred = $q.defer();
		$http.get('/projects').success(function(data, status, headers, config) {
			// client 정렬
			var filtered = params.search.predicateObject ? $filter('filter')(data.projects, params.search.predicateObject) : data.projects;
			if (params.sort.predicate) {
				filtered = $filter('orderBy')(filtered, params.sort.predicate, params.sort.reverse);
			}
			var result = filtered.slice(start, start + number);
			deferred.resolve({
				data : result,
				numberOfPages : Math.ceil(filtered.length / number)
			});
		}).error(function(data, status, headers, config) {
			console.log(status);
		});

		return deferred.promise;
	};

	return projectFactory;

});

app.factory('projectDetailFactory', function($q, $filter, $timeout, $http, cfpLoadingBar) {
	var projectDetailFactory = {};
	projectDetailFactory.getFiles = function(id, start, number, params) {
		console.log(params);
		var deferred = $q.defer();
		$http.get('/project/' + id + '/packages').success(function(data, status, headers, config) {
			// client 정렬
			var filtered = params.search.predicateObject ? $filter('filter')(data.projects, params.search.predicateObject) : data.content;
			if (params.sort.predicate) {
				filtered = $filter('orderBy')(filtered, params.sort.predicate, params.sort.reverse);
			}
			var result = filtered.slice(start, start + number);
			deferred.resolve({
				data : result,
				numberOfPages : Math.ceil(filtered.length / number)
			});
		}).error(function(data, status, headers, config) {
			console.log(status);
		});

		return deferred.promise;
	};

	projectDetailFactory.get = function(id) {
		return $http.get('/project/' + id);
	};
	
	projectDetailFactory.delete = function(id) {
        return $http.delete('/dev/package/' + id);
    };
    
	projectDetailFactory.build = function(id) {
		var logEl = $('#projectLog').find('pre');
		logEl.html("");
		var xhttp = new XMLHttpRequest();
		var length = 0;
		var deferred = $q.defer();
		xhttp.open("POST", '/dev/build/'+id, true);
		xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		xhttp.onreadystatechange = function() {
			if (xhttp.readyState == 2) {
				cfpLoadingBar.start();
			}
			if (xhttp.readyState > 2 && xhttp.status == 200) {
				var result = xhttp.responseText.substring(length, xhttp.responseText.length)
				length = xhttp.responseText.length;
				logEl.append('<code>' + result + '</code>');
				$(document).scrollTop($(document).height());
			}
			if (xhttp.readyState == 4) {
				cfpLoadingBar.complete();
				deferred.resolve();
			}
			console.log(xhttp.status);
		};
		xhttp.send();
		return deferred.promise;
	};
	projectDetailFactory.standardbuild = function(id) {
		$http.get('/dev/build/' + id).success(function(data, status, headers, config) {
			console.log(status);
		}).error(function(data, status, headers, config) {
			console.log(status);
		});
	};
	
	projectDetailFactory.checksource = function(id) {
		var logEl = $('#projectLog').find('pre');
		logEl.html("");
		var xhttp = new XMLHttpRequest();
		var length = 0;
		var deferred = $q.defer();
		xhttp.open("POST", '/dev/checksource/'+id, true);
		xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		xhttp.onreadystatechange = function() {
			if (xhttp.readyState == 2) {
				cfpLoadingBar.start();
			}
			if (xhttp.readyState > 2 && xhttp.status == 200) {
				var result = xhttp.responseText.substring(length, xhttp.responseText.length)
				length = xhttp.responseText.length;
				logEl.append('<code>' + result + '</code>');
				$(document).scrollTop($(document).height());
			}
			if (xhttp.readyState == 4) {
				console.log('');
				cfpLoadingBar.complete();
				deferred.resolve();
			}
			
			if (xhttp.readyState > 2 && xhttp.status == 403) {
				console.log('권한오류');
			}
			console.log(xhttp.status);
		};
		xhttp.send();
		return deferred.promise;
	};
    
	return projectDetailFactory;

});
