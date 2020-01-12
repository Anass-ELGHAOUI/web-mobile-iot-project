var app = new Vue({
    el: '#getBuildings',
    data () {
        return {
            buildings: null,
        }
    },
    mounted () {
        this.getFunction()
    },
    methods: {
        getFunction () {
            axios
                .get('https://walid-ouchtiti.cleverapps.io/api/buildings')
                .then(response => (this.buildings = response.data))
        }
    }
})