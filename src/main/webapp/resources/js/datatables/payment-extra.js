var monthFilters = [{"value":1, "label":"Jan"},{"value":2, "label":"Feb"},{"value":3, "label":"Mar"},
    {"value":4, "label":"Apr"},{"value":5, "label":"May"},{"value":6, "label":"June"},
    {"value":7, "label":"Jul"},{"value":8, "label":"Aug"},{"value":9, "label":"Sept"},
    {"value":10, "label":"Oct"},{"value":11, "label":"Nov"},{"value":12, "label":"Dec"}];
var barangayFilters=["Abihilan", "Poblacion", "Tugas"]
var yearFilters = [];
for (i = new Date().getFullYear(); i >= 2007; i--)
{    yearFilters.push(i);
}
yadcf.init(oTable_payment, [
    {
        "externally_triggered": true,
        "filter_type": "text",
        "filter_container_id": "payment-id",
        "column_number": 0,
        "filter_reset_button_text": false
    },
    {
        "externally_triggered": true,
        "filter_type": "text",
        "filter_container_id": "acct-no",
        "column_number": 1,
        "filter_reset_button_text": false
    },
    {
        "externally_triggered": true,
        "data": monthFilters,
        "filter_type": "select",
        "filter_container_id": "payment-month",
        "column_number": 2,
        "sort_as": "numerical",
        "filter_reset_button_text": false
    },
    {
        "externally_triggered": true,
        "data": yearFilters,
        "filter_type": "select",
        "filter_container_id": "payment-year",
        "column_number": 3,
        "sort_as": "numerical",
        "filter_reset_button_text": false
    },
    {
        "externally_triggered": true,
        "filter_type": "text",
        "filter_container_id": "acct-lastname",
        "column_number": 4,
        "filter_reset_button_text": false
    },
    {
        "externally_triggered": true,
        "filter_type": "text",
        "filter_container_id": "acct-firstname",
        "column_number": 5,
        "filter_reset_button_text": false
    },
    {
        "externally_triggered": true,
        "data": barangayFilters,
        "filter_type": "select",
        "filter_container_id": "acct-brgy",
        "column_number": 6,
        "filter_reset_button_text": false
    },
    {
        "externally_triggered": true,
        "column_number" : 11,
        "filter_container_id": "payment-date",
        "filter_type": 'range_date',
        "date_format": "yyyy/mm/dd",
        "filter_reset_button_text": false
    }], 'none');