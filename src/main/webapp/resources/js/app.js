(function(config){
    if (window.hasOwnProperty('require')) {
        require.config(config);
    } else {
        window.require = config;
    }
})({
    "baseUrl": "resources/js/lib",
    "paths": {
        "app": "../app"
    },
});