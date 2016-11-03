'use strict';

/* Services */


// Demonstrate how to register services
// In this case it is a simple value service.
//angular.module('myApp.services', []).
//  value('version', '0.1');



app.factory('historyFactory', function($q, $filter, $timeout, $http) {
	var historyFactory = {};
    
    historyFactory.getHistoryPagination = function(start, offset, params) {
        var deferred = $q.defer();
        var direction = params.sort.reverse == undefined ? "desc" : params.sort.reverse == true ? "desc" : "asc";
        var result = {};
        var totalPage;     
        	
        $http.get('/history/download', {
                params: {
                    page: start / offset,
                    offset: offset,
                    property: params.sort.predicate,
                    direction: direction
                }
            }).success(function(data, status, headers, config) {
                if (data) {
                    result = data.content;
                    totalPage = parseInt(data.pageInfo.total / data.pageInfo.offset) + 1;
                }
                deferred.resolve({
                    data: result,
                    numberOfPages: totalPage
                });
            })
            .error(function(data, status, headers, config) {
                console.log(status);
            });

        return deferred.promise;
    };
    
    historyFactory.getHistoryUpgradePagination = function(start, offset, params) {
        var deferred = $q.defer();
        var direction = params.sort.reverse == true ? "desc" : "asc";
        var result = {};
        var totalPage;     
        	
        $http.get('/history/upgrade', {
                params: {
                    page: start / offset,
                    offset: offset,
                    property: params.sort.predicate,
                    direction: direction
                }
            }).success(function(data, status, headers, config) {
                if (data) {
                    result = data.content;
                    totalPage = parseInt(data.pageInfo.total / data.pageInfo.offset) + 1;
                }
                deferred.resolve({
                    data: result,
                    numberOfPages: totalPage
                });
            })
            .error(function(data, status, headers, config) {
                console.log(status);
            });

        return deferred.promise;
    };
    
    return historyFactory;
});