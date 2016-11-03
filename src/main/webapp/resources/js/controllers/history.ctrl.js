'use strict';


app.controller('historyListCtrl', function($scope, $http, historyFactory) {
	var ctrl = this;

	this.displayed = [];

	this.callServer = function callServer(tableState) {

		ctrl.isLoading = true;

		var pagination = tableState.pagination;
		var start = pagination.start || 0; // This is NOT the page number, but the index of item in the list that you want to use to display the table.
		var offset = pagination.number || 10; // Number of entries showed per page.

		historyFactory.getHistoryPagination(start, offset, tableState).then(function(result) {
			ctrl.displayed = result.data;
			console.log(ctrl.displayed);
			tableState.pagination.numberOfPages = result.numberOfPages; //set the number of pages so the pagination can update
			ctrl.isLoading = false;
			console.log(ctrl.displayed);
		});
	};
});

app.controller('historyUpgradeListCtrl', function($scope, $http, historyFactory) {
	var ctrl = this;

	this.displayed = [];

	this.callServer = function callServer(tableState) {

		ctrl.isLoading = true;

		var pagination = tableState.pagination;
		var start = pagination.start || 0; // This is NOT the page number, but the index of item in the list that you want to use to display the table.
		var offset = pagination.number || 10; // Number of entries showed per page.

		historyFactory.getHistoryUpgradePagination(start, offset, tableState).then(function(result) {
			ctrl.displayed = result.data;
			console.log(ctrl.displayed);
			tableState.pagination.numberOfPages = result.numberOfPages; //set the number of pages so the pagination can update
			ctrl.isLoading = false;
			console.log(ctrl.displayed);
		});
	};
});