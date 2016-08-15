var typeFilters = [{"value":1, "label": "Wage(1-15)"}, {"value":2, "label": "Wage(16-30)"}, {"value":3, "label":"Power Usage"}]
var monthFilters = [{"value":1, "label":"Jan"},{"value":2, "label":"Feb"},{"value":3, "label":"Mar"},
    {"value":4, "label":"Apr"},{"value":5, "label":"May"},{"value":6, "label":"June"},
    {"value":7, "label":"Jul"},{"value":8, "label":"Aug"},{"value":9, "label":"Sep"},
    {"value":10, "label":"Oct"},{"value":11, "label":"Nov"},{"value":12, "label":"Dec"}];
var yearFilters = [];
for (i = new Date().getFullYear(); i >= 2007; i--)
{    yearFilters.push(i);
}
yadcf.init(oTable_expense, [
    {
        "externally_triggered": true,
        "data": monthFilters,
        "filter_type": "select",
        "filter_container_id": "expense-month",
        "column_number": 1,
        "sort_as": "numerical",
        "filter_reset_button_text": '<i class="fa fa-remove fa-fw"></i>'
    },
    {
        "externally_triggered": true,
        "data": yearFilters,
        "filter_type": "select",
        "filter_container_id": "expense-year",
        "column_number": 2,
        "sort_as": "numerical",
        "filter_reset_button_text": '<i class="fa fa-remove fa-fw"></i>'
    },
    {
        "externally_triggered": true,
        "data": typeFilters,
        "filter_type": "select",
        "filter_container_id": "expense-type",
        "column_number": 4,
        "filter_reset_button_text": '<i class="fa fa-remove fa-fw"></i>'
    }], 'none');
/*
var month = new Date().getMonth()+1;
var year = new Date().getFullYear();
if(month-1 === 0){
    month = 12;
    year--;
} else month--;
yadcf.exFilterColumn(oTable_expense, [
    [1, month],
    [2, year]
]);*/
