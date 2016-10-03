$(document).ready(function(){
    Chart.defaults.groupableBar = Chart.helpers.clone(Chart.defaults.bar);
    Chart.controllers.groupableBar = Chart.controllers.bar.extend({
        calculateBarX: function (index, datasetIndex) {
            // position the bars based on the stack index
            var stackIndex = this.getMeta().stackIndex;
            return Chart.controllers.bar.prototype.calculateBarX.apply(this, [index, stackIndex]);
        },

        hideOtherStacks: function (datasetIndex) {
            var meta = this.getMeta();
            var stackIndex = meta.stackIndex;

            this.hiddens = [];
            for (var i = 0; i < datasetIndex; i++) {
                var dsMeta = this.chart.getDatasetMeta(i);
                if (dsMeta.stackIndex !== stackIndex) {
                    this.hiddens.push(dsMeta.hidden);
                    dsMeta.hidden = true;
                }
            }
        },

        unhideOtherStacks: function (datasetIndex) {
            var meta = this.getMeta();
            var stackIndex = meta.stackIndex;

            for (var i = 0; i < datasetIndex; i++) {
                var dsMeta = this.chart.getDatasetMeta(i);
                if (dsMeta.stackIndex !== stackIndex) {
                    dsMeta.hidden = this.hiddens.unshift();
                }
            }
        },

        calculateBarY: function (index, datasetIndex) {
            this.hideOtherStacks(datasetIndex);
            var barY = Chart.controllers.bar.prototype.calculateBarY.apply(this, [index, datasetIndex]);
            this.unhideOtherStacks(datasetIndex);
            return barY;
        },

        calculateBarBase: function (datasetIndex, index) {
            this.hideOtherStacks(datasetIndex);
            var barBase = Chart.controllers.bar.prototype.calculateBarBase.apply(this, [datasetIndex, index]);
            this.unhideOtherStacks(datasetIndex);
            return barBase;
        },

        getBarCount: function () {
            var stacks = [];
            // put the stack index in the dataset meta
            Chart.helpers.each(this.chart.data.datasets, function (dataset, datasetIndex) {
                var meta = this.chart.getDatasetMeta(datasetIndex);
                if (meta.bar && this.chart.isDatasetVisible(datasetIndex)) {
                    var stackIndex = stacks.indexOf(dataset.stack);
                    if (stackIndex === -1) {
                        stackIndex = stacks.length;
                        stacks.push(dataset.stack);
                    }
                    meta.stackIndex = stackIndex;
                }
            }, this);

            this.getMeta().stacks = stacks;
            return stacks.length;
        },
    });
    var chartOptions = {
        scales: {
            yAxes: [{
                ticks: {
                    beginAtZero: true
                },
                stacked: true
            }]
        },
        maintainAspectRatio: false
    };
    var canvasList = [];
    $('#charts canvas').each(function(index){
        canvasList.push($(this));
    });
    var currentChart;
    function initFirstChart(){
        $.when(getChartData(1)).then(function(response){
            var chart = canvasList[0];
            var loader = chart.parent().find('div.chart-loader i');
            var noData = chart.parent().find('div.chart-loader p');
            loader.hide();
            if(response.status === "SUCCESS")
                chart.show();
            else noData.show();
            currentChart = new Chart(chart, {
                type: 'groupableBar',
                data: response.result,
                options: chartOptions
            });
        });
    }
    function getChartData(chartIndex){
        var header = $("meta[name='_csrf_header']").attr("content");
        var token = $("meta[name='_csrf']").attr("content");
        var serviceUri = $('#admin-uri').val()+"get-chart-data";
        return $.ajax({
            url: serviceUri,
            data: {chart: chartIndex},
            type: "POST",
            beforeSend: function(xhr){
                xhr.setRequestHeader(header, token);
            }
        })
    }
    $('#chart-menu li a').on('shown.bs.tab', function() {
        var chartIndex = $(this).parent('li').index();
        currentChart.destroy();
        var chart = canvasList[chartIndex];
        var loader = chart.parent().find('div.chart-loader i');
        var noData = chart.parent().find('div.chart-loader p');
        loader.show();
        chart.hide();
        noData.hide();
        $.when(getChartData(chartIndex+1)).then(function(response){
            loader.hide();
            if(response.status === "SUCCESS")
                chart.show();
            else noData.show();
            currentChart = new Chart(chart, {
                type: 'groupableBar',
                data: response.result,
                options: chartOptions
            });

        });
    });
    initFirstChart();
});