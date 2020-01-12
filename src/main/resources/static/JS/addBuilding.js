var addroom = new Vue({
    el: '#addroom',
    data () {
        return {
            /* HTTP data */
            rooms: null,
            /* Form data (v-model) */
            nbOfFloors: 0,
            buildingName: "",
            roomsList: [],
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
        addBuilding() {
            const requestBody = {
                name: this.buildingName,
                nbOfFloors: this.nbOfFloors,
                roomsIds: this.roomsList,
            };
            axios
                .post('https://walid-ouchtiti.cleverapps.io/api/buildings', requestBody)
                .then(response => {this.error = false})
                .catch(error => {
                    this.error = true
                });
        }
    }
})
