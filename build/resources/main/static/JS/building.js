var app = new Vue({
    el: '#app',
    data () {
        return {
            building: null,
            buildingId: null,
            rooms: null,
            ringer: null,
            loading: true,
            errored: false,
            deleteMessage: "",
            editMessage: "",
        }
    },
    mounted () {
        this.getFunction();
        this.getBuildingInfo();
    },
    created() {
        let uri = window.location.search.substring(1);
        let urlParams = new URLSearchParams(uri);
        this.buildingId = urlParams.get("building");
    },
    methods : {
        getBuildingInfo() {
            /* Add building name as title in the page once loaded */
            axios
                .get('https://walid-ouchtiti.cleverapps.io/api/buildings/' + this.buildingId)
                .then(response => {
                    this.building = response.data;
                })
        },
        getFunction() {
            axios
                .get('https://walid-ouchtiti.cleverapps.io/api/rooms')
                .then(response => {
                    this.rooms = response.data
                })
                .catch(error => {
                    this.errored = true
                })
                .finally(() => this.loading = false);
        },
        autoRefresh(){
            setInterval(() => {
                this.getFunction();
            },2000);
        },
        deleteRoom(roomId) {
            axios
                .delete('https://walid-ouchtiti.cleverapps.io/api/rooms/' + roomId)
                .then(
                    buildingId = this.buildingId,
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
                        window.location.href = 'building.html?building=' + buildingId;
                    }, 3000),

                )
                .catch(error => {
                    this.deleteMessage = "problem"
                })
        },
        editRoom(roomId) {
            /* Enter edit mode (create input fields) */
            if (document.getElementById("editMode" + roomId) === null) {
                /* Create hidden input to know we entered edit mode */
                var hidden = document.createElement('input');
                hidden.setAttribute("type", "hidden");
                hidden.setAttribute("id", "editMode" + roomId);
                document.getElementById('app').appendChild(hidden);

                // window.location.href = 'addRoom.html?room=' + roomId;
                var roomName = document.getElementById('roomName' + roomId);
                var roomFloor = document.getElementById('roomFloor' + roomId);
                var editButton = document.getElementById('edit' + roomId);

                /* Change button text */
                editButton.textContent = "Submit changes"

                /* Create text input to edit room */
                var roomNameInput = document.createElement('input');
                roomNameInput.setAttribute("type", "text");
                roomNameInput.setAttribute("id", "roomNewName" + roomId);
                roomNameInput.required = true;
                roomNameInput.setAttribute("class", "form-control mb-4");
                roomNameInput.setAttribute("value", roomName.textContent);
                roomName.innerHTML = "";
                roomName.appendChild(roomNameInput);

                /* Create list to choose new floor */
                var roomFloorList = document.createElement('select');
                roomFloorList.setAttribute("id", "roomFloorList" + roomId);
                roomFloorList.required = true;
                roomFloorList.setAttribute("class", "class=\"browser-default custom-select mb-4\"")
                axios
                    .get('https://walid-ouchtiti.cleverapps.io/api/buildings/' + this.buildingId)
                    .then(response => {
                        var building = response.data;
                        var nbOfFloors = building.nbOfFloors;

                        var floor = document.createElement('option');
                        floor.innerHTML = "Choose a floor";
                        roomFloorList.appendChild(floor);

                        for (var i = 0; i <= nbOfFloors; i++) {
                            var floor = document.createElement('option');
                            floor.setAttribute("value", i);
                            floor.innerHTML = i;
                            if (i == roomFloor.textContent) {
                                floor.selected = true;
                            }
                            roomFloorList.appendChild(floor);
                        }
                        roomFloor.innerHTML = "";
                        roomFloor.appendChild(roomFloorList);
                    })
            }

            /* Apply changes to the room */
            else {
                /* Get room previous data */
                axios
                    .get('https://walid-ouchtiti.cleverapps.io/api/rooms/' + roomId)
                    .then(response => {
                        var room = response.data;

                        var roomName = document.getElementById("roomNewName" + roomId).value;
                        var roomFloorList = document.getElementById("roomFloorList" + roomId);
                        var roomFloor = roomFloorList.options[roomFloorList.selectedIndex].value;

                        const requestBody = {
                            id: room.id,
                            name: roomName,
                            floor: roomFloor,
                            lightsIds: room.lightsIds,
                            ringerId: room.ringerId,
                            buildingId: room.buildingId,
                        };
                        /* Send the http request to change the data */
                        axios
                            .post('https://walid-ouchtiti.cleverapps.io/api/rooms', requestBody)
                            .then(response => {this.editMessage = "success";

                                /* Timer before reloading page */
                                setTimeout(function(){
                                    document.getElementById('editMessage').innerHTML = "The data has been successfully edited, you will be redirected to the page after 2 seconds"
                                }, 1000),
                                    setTimeout(function(){
                                        document.getElementById('editMessage').innerHTML = "The data has been successfully edited, you will be redirected to the page after 1 second"
                                    }, 2000),

                                    /* Reload the page to refresh info */
                                    setTimeout(function(){
                                        let uri = window.location.search.substring(1);
                                        let urlParams = new URLSearchParams(uri);
                                        this.buildingId = urlParams.get("building");
                                        window.location.href = 'building.html?building=' + this.buildingId;
                                    }, 3000)
                            })
                            .catch(error => {
                                this.editMessage = "problem";
                            });
                    })
            }
        },
        editBuilding() {
            this.getBuildingInfo();
            /* Enter edit mode (create input fields) */
            if (document.getElementById("BuildingEditMode") === null) {
                /* Create hidden input to know we entered edit mode */
                var hidden = document.createElement('input');
                hidden.setAttribute("type", "hidden");
                hidden.setAttribute("id", "BuildingEditMode");
                document.getElementById('app').appendChild(hidden);

                // window.location.href = 'addRoom.html?room=' + roomId;
                var buildingName = document.getElementById('buildingName');
                var editButton = document.getElementById('editBuilding');

                /* Change button text */
                editButton.textContent = "Submit changes"

                /* Create text input to edit building */
                var buildingNameInput = document.createElement('input');
                buildingNameInput.setAttribute("type", "text");
                buildingNameInput.setAttribute("id", "buildingNewName");
                buildingNameInput.required = true;
                buildingNameInput.setAttribute("class", "form-control mb-4");
                buildingNameInput.setAttribute("value", this.building.name);
                buildingName.innerHTML = "";
                document.getElementById('buildingInfo').appendChild(buildingNameInput);

                /* Create list to choose new number of floors */
                var nbOfFloors = document.createElement('input');
                nbOfFloors.setAttribute("type", "number");
                nbOfFloors.setAttribute("min", "1");
                nbOfFloors.setAttribute("max", "50");
                nbOfFloors.setAttribute("id", "buildingNewNbOfFloors");
                nbOfFloors.required = true;
                nbOfFloors.setAttribute("class", "form-control mb-4");
                nbOfFloors.setAttribute("value", this.building.nbOfFloors);
                document.getElementById('buildingInfo').appendChild(nbOfFloors);
            }

            /* Apply changes to the room */
            else {
                /* Get building previous data */
                axios
                    .get('https://walid-ouchtiti.cleverapps.io/api/buildings/' + this.buildingId)
                    .then(response => {
                        var building = response.data;

                        var buildingName = document.getElementById("buildingNewName").value;
                        var nbOfFloors = document.getElementById("buildingNewNbOfFloors").value;

                        /* If the new number of floors is lower than the previous one delete all the rooms in the removed floors */
                        if (nbOfFloors < building.nbOfFloors) {
                            /* Get all the rooms in the no more existing floors */
                            axios
                                .get('https://walid-ouchtiti.cleverapps.io/api/rooms')
                                .then(response => {
                                    var rooms = response.data;

                                    for (var i = 0; i < rooms.length; i++) {
                                        if (rooms[i].floor > nbOfFloors) {
                                            /* Delete the rooms */
                                            axios
                                                .delete('https://walid-ouchtiti.cleverapps.io/api/rooms/' + rooms[i].id)
                                                .catch(error => {
                                                })
                                        }
                                    }
                                })
                                .catch(error => {
                                })
                        }

                        const requestBody = {
                            id: building.id,
                            name: buildingName,
                            nbOfFloors: nbOfFloors,
                            roomsIds: building.roomsIds,
                        };
                        /* Send the http request to change the data */
                        axios
                            .post('https://walid-ouchtiti.cleverapps.io/api/buildings', requestBody)
                            .then(response => {this.editMessage = "success";

                                /* Timer before reloading page */
                                setTimeout(function() {
                                    document.getElementById('editMessage').innerHTML = "The data has been successfully edited, you will be redirected to the page after 2 seconds"
                                }, 1000),
                                    setTimeout(function() {
                                        document.getElementById('editMessage').innerHTML = "The data has been successfully edited, you will be redirected to the page after 1 second"
                                    }, 2000),

                                    /* Reload the page to refresh info */
                                    setTimeout(function() {
                                        let uri = window.location.search.substring(1);
                                        let urlParams = new URLSearchParams(uri);
                                        this.buildingId = urlParams.get("building");
                                        window.location.href = 'building.html?building=' + this.buildingId;
                                    }, 3000)
                            })
                            .catch(error => {
                                this.editMessage = "problem";
                            });
                    })
            }
        },
        deleteBuilding() {
            axios
                .delete('https://walid-ouchtiti.cleverapps.io/api/buildings/' + this.buildingId)
                .then(
                    buildingId = this.buildingId,
                    document.getElementById('loading').hidden = false,
                    this.deleteMessage = "success",

                    /* Timer before reloading page */
                    setTimeout(function() {
                        document.getElementById('deleteMessage').innerHTML = "The data has been successfully deleted, you will be redirected to the page after 2 seconds"
                    }, 1000),
                    setTimeout(function() {
                        document.getElementById('deleteMessage').innerHTML = "The data has been successfully deleted, you will be redirected to the page after 1 second"
                    }, 2000),

                    /* Reload the page to refresh info */
                    setTimeout(function() {
                        window.location.href = 'index.html';
                    }, 3000),

                )
                .catch(error => {
                    this.deleteMessage = "problem"
                })
        }
    }
})
