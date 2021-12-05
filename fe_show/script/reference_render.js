let sigmaSettings = {
    minNodeSize: 5,
    maxNodeSize: 20,
    minEdgeSize: 3,
    maxEdgeSize: 10,
    edgeHoverSizeRatio: 2
}
s = new sigma({
    graph: graphJson,
    container: 'sigma-container',
    settings: sigmaSettings
});
const config = {
    nodeMargin: 5.0,
    scaleNodes: 1.4
};

// Configure the algorithm
const listener = s.configNoverlap(config);

// Bind all events:
listener.bind('start stop interpolate', function(event) {
    console.log(event.type);
});
// Start the algorithm:
s.startNoverlap();