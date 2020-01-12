var addroom = new Vue({
    el: '#addroom',
    data () {
        return {
            /* HTTP data */
            rooms: null,
            /* Form data (v-model) */
            roomId: "",
            /* Error handling */
            error: null,
        }
    },
    mounted () {
        this.getRooms();
    },
    methods : {
        getRooms() {
            axios
                .get('https://walid-ouchtiti.cleverapps.io/api/rooms')
                .then(response => (this.rooms = response.data))
        },
        addThermostat() {
            const requestBody = {
                level: 0,
                status: "OFF",
                onTemperature: "10",
                offTemperature: "18",
                roomId: this.roomId,
            };
            axios
                .post('https://walid-ouchtiti.cleverapps.io/api/thermostats', requestBody)
                .then(response => {this.error = false})
                .catch(error => {
                    this.error = true
                });
        }
    }
})
