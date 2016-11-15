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


	projectFactory.buildStandardProject = function(prj, version, branch) {
		$('#buildBtn').hide();
		var logEl = $('#log').find('pre');
		logEl.css('display', 'block');
		var xhttp = new XMLHttpRequest();
		var length = 0;
		// version, branch, maild_path, maild_version, site
		var params = "version=" + version + "&branch=" + branch + "&maild_path=&maild_version=&site=" + prj.path;
		xhttp.open("POST", '/project/build/standard', true);
		xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		xhttp.onreadystatechange = function() {
			if (xhttp.readyState == 2) {
				cfpLoadingBar.start();
			}
			if (xhttp.readyState > 2 && xhttp.status == 200) {
				var result = xhttp.responseText.substring(length, xhttp.responseText.length)
				length = xhttp.responseText.length;
				logEl.append('<span>' + result + '</span>');
				$('#log').scrollTop($('#log')[0].scrollHeight);
			}
			if (xhttp.readyState == 4) {
				cfpLoadingBar.complete();
			}
		};
		xhttp.send(params);
	};

	projectFactory.mobileBuild = function(prj, version) {
		console.log(prj);
		console.log(version);
		// http://172.21.26.191:8000/DOFactory/brandtable?version=2.3.5&companyname=유비온&platform=all
		// http://172.21.27.40:8000/build/totaldownloads?appName=UBION2.3.5.0&version=2.3.5&platform=iOS
		var name = prj.name;
		$http.get('/project/version?project=' + prj.path).success(function(data, status, headers, config) {
			console.log(data);
			var version = data.value.substr(0, 5);
			if (version.length < 1) {
				version = 'all';
			}
			window.open("http://172.21.26.191:8000/DOFactory/brandtable?version=" + version + "&companyname=" + name + "&platform=all", "New Window", "width=950,height=750,resizable=1");
		}).error(function(data, status, headers, config) {
			console.log(status);
		});
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
				$('#projectLog').scrollTop($('#projectLog')[0].scrollHeight);
			}
			if (xhttp.readyState == 4) {
				cfpLoadingBar.complete();
				deferred.resolve();
			}
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
				$('#projectLog').scrollTop($('#projectLog')[0].scrollHeight);
			}
			if (xhttp.readyState == 4) {
				console.log('');
				cfpLoadingBar.complete();
				deferred.resolve();
			}
		};
		xhttp.send();
		return deferred.promise;
	};
    
	return projectDetailFactory;

});
