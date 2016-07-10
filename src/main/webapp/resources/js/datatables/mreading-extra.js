var monthFilters = [{"value":1, "label":"Jan"},{"value":2, "label":"Feb"},{"value":3, "label":"Mar"},
    {"value":4, "label":"Apr"},{"value":5, "label":"May"},{"value":6, "label":"June"},
    {"value":7, "label":"Jul"},{"value":8, "label":"Aug"},{"value":9, "label":"Sept"},
    {"value":10, "label":"Oct"},{"value":11, "label":"Nov"},{"value":12, "label":"Dec"}];
var yearFilters = [];
for (i = new Date().getFullYear(); i >= 2007; i--)
{    yearFilters.push(i);
}
var barangayFilters=["Abihilan", "Poblacion", "Tugas"]
yadcf.init(oTable_reading, [
    {
        "externally_triggered": true,
        "filter_type": "text",
        "filter_container_id": "acct-no",
        "column_number": 1,
        "filter_reset_button_text": false
    },{
        "externally_triggered": true,
        "filter_type": "text",
        "filter_container_id": "acct-lastname",
        "column_number": 2,
        "filter_reset_button_text": false
    },{
        "externally_triggered": true,
        "filter_type": "text",
        "filter_container_id": "acct-firstname",
        "column_number": 3,
        "filter_reset_button_text": false
    }, {
        "externally_triggered": true,
        "data": monthFilters,
        "filter_type": "select",
        "filter_container_id": "reading-month",
        "column_number": 4,
        "sort_as": "numerical",
        "filter_reset_button_text": false
    }, {
        "externally_triggered": true,
        "data": yearFilters,
        "filter_type": "select",
        "filter_container_id": "reading-year",
        "column_number": 5,
        "sort_as": "numerical",
        "filter_reset_button_text": false
    }, {
        "externally_triggered": true,
        "data": barangayFilters,
        "filter_type": "select",
        "filter_container_id": "acct-brgy",
        "column_number": 6,
        "filter_reset_button_text": false
    }], 'none');
var month = new Date().getMonth()+1;
var year = new Date().getFullYear();
if(month-1 === 0){
    month = 12;
    year--;
} else month--;
yadcf.exFilterColumn(oTable_reading, [
 [4, month],
 [5, year]
]); 
