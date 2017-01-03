var monthFilters = [{"value":1, "label":"Jan"},{"value":2, "label":"Feb"},{"value":3, "label":"Mar"},
    {"value":4, "label":"Apr"},{"value":5, "label":"May"},{"value":6, "label":"Jun"},
    {"value":7, "label":"Jul"},{"value":8, "label":"Aug"},{"value":9, "label":"Sep"},
    {"value":10, "label":"Oct"},{"value":11, "label":"Nov"},{"value":12, "label":"Dec"}];
var yearFilters = [];
var zoneFilters=[1,2,3,4,5];
var invStatFilters = ["UNPAID", "PARTIALLYPAID", "FULLYPAID"];
for (var i = new Date().getFullYear(); i >= 2007; i--) {
    yearFilters.push(i);
};
var barangayFilters=["Cogtong", "Tawid", "Can-olin", "Cadapdapan", "Tambongan",
    "Abihilan", "La Union", "Panadtaran", "Poblacion", "Boyoan", "Pagahat"];
yadcf.init(oTable_invoice, [
    {
        "externally_triggered": true,
        "filter_type": "text",
        "filter_container_id": "acct-no",
        "column_number": 2,
        "filter_reset_button_text": '<i class="fa fa-remove fa-fw"></i>'
    },
    {
        "externally_triggered": true,
        "filter_type": "text",
        "filter_container_id": "acct-lastname",
        "column_number": 3,
        "filter_reset_button_text": '<i class="fa fa-remove fa-fw"></i>'
    },
    {
        "externally_triggered": true,
        "filter_type": "text",
        "filter_container_id": "acct-firstname",
        "column_number": 4,
        "filter_reset_button_text": '<i class="fa fa-remove fa-fw"></i>'
    },
    {
        "externally_triggered": true,
        "data": monthFilters,
        "filter_type": "select",
        "filter_container_id": "reading-month",
        "column_number": 5,
        "sort_as": "numerical",
        "filter_reset_button_text": '<i class="fa fa-remove fa-fw"></i>'
    }, 
    {
        "externally_triggered": true,
        "data": yearFilters,
        "filter_type": "select",
        "filter_container_id": "reading-year",
        "column_number": 6,
        "sort_as": "numerical",
        "filter_reset_button_text": '<i class="fa fa-remove fa-fw"></i>'
    },
    {
        "externally_triggered": true,
        "data": barangayFilters,
        "filter_type": "select",
        "filter_container_id": "acct-brgy",
        "column_number": 7,
        "filter_reset_button_text": '<i class="fa fa-remove fa-fw"></i>'
    },{
        "externally_triggered": true,
        "data": zoneFilters,
        "filter_type": "select",
        "filter_container_id": "acct-zone",
        "column_number": 8,
        "sort_as": "numerical",
        "filter_reset_button_text": '<i class="fa fa-remove fa-fw"></i>'
    },{
        "externally_triggered": true,
        "data": invStatFilters,
        "filter_type": "select",
        "filter_container_id": "inv-st",
        "column_number": 9,
        "filter_reset_button_text": '<i class="fa fa-remove fa-fw"></i>'
    },], 'none');
/*
var month = new Date().getMonth()+1;
var year = new Date().getFullYear();
if(month-1 === 0){
    month = 12;
    year--;
} else month--;
yadcf.exFilterColumn(oTable_invoice, [
 [5, month],
 [6, year]
]);*/
