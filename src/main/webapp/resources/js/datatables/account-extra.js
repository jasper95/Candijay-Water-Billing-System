var barangayFilters=["Abihilan", "Poblacion", "Tugas"]
var statusFilters=['ACTIVE', 'WARNING', 'INACTIVE'];
yadcf.init(oTable_account, [
    {
        "externally_triggered": true,
        "filter_type": "text",
        "filter_container_id": "acct-no",
        "column_number": 0,
        "filter_reset_button_text": false
    },{
        "externally_triggered": true,
        "filter_type": "text",
        "filter_container_id": "acct-lastname",
        "column_number": 1,
        "filter_reset_button_text": false
    },{
        "externally_triggered": true,
        "filter_type": "text",
        "filter_container_id": "acct-firstname",
        "column_number": 2,
        "filter_reset_button_text": false
    },{
        "externally_triggered": true,
        "data": barangayFilters,
        "filter_type": "select",
        "filter_container_id": "acct-brgy",
        "column_number": 6,
        "filter_reset_button_text": false
    },{
        "externally_triggered": true,
        "data": statusFilters,
        "filter_type": "select",
        "filter_container_id": "acct-status",
        "column_number": 8,
        "filter_reset_button_text": false
    }], 'none');