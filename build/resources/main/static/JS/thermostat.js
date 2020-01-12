var app = new Vue({
    el: '#app',
    data () {
        return {
            roomController: null,
            room: null,
            roomId: null,
            thermostats: null,
            loading: true,
            deleteMessage: "",
            errored: false
        }
    },
    mounted () {
        this.getFunction();
        this.getRoomInfo();
        this.getRoomAutoThermostatControllerInfo();
    },
    created() {
        let uri = window.location.search.substring(1);
        let urlParams = new URLSearchParams(uri);
        this.roomId = urlParams.get("room");
    },
    methods : {
        getRoomAutoThermostatControllerInfo() {
            axios
                .get('https://walid-ouchtiti.cleverapps.io/api/autoLightControllers/')
                .then(response => {
                    var autoLightControllers = response.data;
                    for (var i = 0; i < autoLightControllers.length; i++) {
                        if (autoLightControllers[i].roomId == this.roomId) {
                            this.roomController = autoLightControllers[i];
                            break;
                        }
                    }
                })
        },
        getRoomInfo() {
            /* Add room name as title in the page once loaded */
            axios
                .get('https://walid-ouchtiti.cleverapps.io/api/rooms/' + this.roomId)
                .then(response => {
                    this.room = response.data;
                })
        },
        getFunction(){
            axios
                .get('https://walid-ouchtiti.cleverapps.io/api/thermostats')
                .then(response => {
                    this.thermostats = response.data
                })
                .catch(error => {
                    this.errored = true
                })
                .finally(() => this.loading = false);
        },
        autoRefresh() {
            setInterval(() => {
                this.getFunction();
            },2000);
        },
        switchThermostat(id) {
            var elt = document.getElementById("thermostat");
            if (elt.textContent === "ON") {
                for (var i = 0; i < this.thermostats.length; i++) {
                    if (this.thermostats[i].id === id) {
                        this.thermostats[i].status = "OFF";
                    }
                }
                document.getElementById("thermostat").textContent = "";
                document.getElementById("thermostat").appendChild(document.createTextNode("OFF"));
                document.getElementById("switchn" + id).innerHTML = '<i class="fas fa-toggle-off fa-2x"></i>';
            }
            else {
                for (var i = 0; i < this.thermostats.length; i++) {
                    if (this.thermostats[i].id === id) {
                        this.thermostats[i].status = "ON";
                    }
                }
                document.getElementById("thermostat").textContent = "";
                document.getElementById("thermostat").appendChild(document.createTextNode("ON"));
                document.getElementById("switchn" + id).innerHTML = '<i class="fas fa-toggle-on fa-2x"></i>';
            }
            axios
                .put('https://walid-ouchtiti.cleverapps.io/api/thermostats/' + id + '/switch')
        },
        changeThermostatLevel(thermostatId) {
            var thermostatLevel = document.getElementById("thermostatLevel" + thermostatId).value;
            const restApiBody = {
                level: parseInt(thermostatLevel),
            };
            axios
                .put('https://walid-ouchtiti.cleverapps.io/api/thermostats/' + thermostatId + '/level', restApiBody)
                .then((response) => {});
        },
        deleteThermostat(thermostatId) {
            axios
                .delete('https://walid-ouchtiti.cleverapps.io/api/thermostats/' + thermostatId)
                .then(
                    roomId = this.roomId,
                    document.getElementById('loading').hidden = false,
                    this.deleteMessage = "success",

                    /* Timer before reloading page */
                    setTimeout(function(){
                        document.getElementById('deleteMessage').innerHTML = "The data has been successfully deleted, you will be redirected to the page after 2 seconds"
                    }, 1000),
                    setTimeout(function(){
                        document.getElementById('deleteMessage').innerHTML = "The data has been successfully deleted, you will be redirected to the page after 1 second"
                    }, 2000),

                    /* Reload the page to refresh info */
                    setTimeout(function(){
                        window.location.href = 'thermostat.html?room=' + roomId;
                    }, 3000),

                )
                .catch(error => {
                    this.deleteMessage = "problem"
                })
        },
        switchAutoThermostatControl(roomId) {
            /* Change the page icons */
            var elt = document.getElementById("auto" + roomId);
            if (elt.className === "fas fa-toggle-on fa-2x") {
                this.roomController.autoThermostatControlState = "OFF"
                elt.className = "fas fa-toggle-off fa-2x";
            }
            else {
                this.roomController.autoThermostatControlState = "ON";
                elt.className = "fas fa-toggle-on fa-2x";
            }

            /* Get user position */
            navigator.geolocation.getCurrentPosition(function(position) {
                /* Get sunset and sunrise hours */
                axios
                    .get('https://api.sunrise-sunset.org/json?lat=' + position.coords.latitude + '&lng=' + position.coords.longitude + '&date=today')
                    .then(response => {
                        var sunTimes = response.data;
                        /* Get the room's auto light controller id */
                        axios
                            .get('https://walid-ouchtiti.cleverapps.io/api/autoLightControllers/')
                            .then(response => {
                                var autoLightControlers = response.data;
                                for (var i = 0; i < autoLightControlers.length; i++) {
                                    if (autoLightControlers[i].roomId == roomId) {
                                        /* Switch the state of the auto controller */
                                        axios
                                            .put('https://walid-ouchtiti.cleverapps.io/api/autoLightControllers/' + autoLightControlers[i].id + '/switchThermostat')

                                        /* Change sunset and sunrise info */
                                        const requestBody = {
                                            sunriseTime: sunTimes.results.sunrise,
                                            sunsetTime: sunTimes.results.sunset,
                                            latitude: position.coords.latitude,
                                            longitude: position.coords.longitude,
                                        };
                                        axios
                                            .put('https://walid-ouchtiti.cleverapps.io/api/autoLightControllers/' + autoLightControlers[i].id + '/sunset-sunrise', requestBody)
                                            .then((response) => {});
                                    }
                                }
                            })

                    })

            });
        },
        changeMinTemperature(roomId) {
            axios
                .get('https://walid-ouchtiti.cleverapps.io/api/autoLightControllers/')
                .then(response => {
                    var autoLightControlers = response.data;
                    for (var i = 0; i < autoLightControlers.length; i++) {
                        if (autoLightControlers[i].roomId == roomId) {
                            var tmp = document.getElementById('minTemperature' + roomId).value;
                            const requestBody = {
                                minTemperature: tmp,
                            };
                            axios
                                .put('https://walid-ouchtiti.cleverapps.io/api/autoLightControllers/' + autoLightControlers[i].id + '/minTemperature', requestBody)
                                .then((response) => {});
                        }
                    }
                })
        }
    }
})
