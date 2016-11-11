'use strict';

/* Controllers */
app.controller('downloadCtrl', function($scope, $routeParams, $filter, downloadFactory) {
	var category = $routeParams.category;
	$scope.category = $routeParams.category;
	var ctrl = this;
	ctrl.displayed = [];
	ctrl.callServer = function callServer(tableState) {
		ctrl.isLoading = true;
		var pagination = tableState.pagination;
		var start = pagination.start || 0; // This is NOT the page number, but the index of item in the list that you want to use to display the table.
		var offset = pagination.number || 10; // Number of entries showed per page.

		downloadFactory.getPackages(category, start, offset, tableState).then(function(result) {
			ctrl.displayed = result.data;
			tableState.pagination.numberOfPages = result.numberOfPages; //set the number of pages so the pagination can update
			ctrl.isLoading = false;
		});
	};

	$scope.download = function(pkgId) {
		window.location = '/dev/package/' + pkgId;
	};
	
});