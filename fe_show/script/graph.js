let graphJson = {
  "nodes": [
    {
      "id": "com.skyinu.vm_eraser.VMEraser",
      "label": "com.skyinu.vm_eraser.VMEraser",
      "color": "#f00",
      "size": 1,
      "x": 500,
      "y": 500,
      "debug_level": 0,
      "debug_angel": 0.0
    },
    {
      "id": "com.skyinu.vm_eraser.MemoryFileParser",
      "label": "com.skyinu.vm_eraser.MemoryFileParser",
      "color": "#ff0",
      "size": 2,
      "x": 500,
      "y": 520,
      "debug_level": 1,
      "debug_angel": 0.0
    },
    {
      "id": "com.skyinu.vm_eraser.MapItemModel",
      "label": "com.skyinu.vm_eraser.MapItemModel",
      "color": "#ff0",
      "size": 2,
      "x": 500,
      "y": 480,
      "debug_level": 1,
      "debug_angel": 180.0
    },
    {
      "id": "com.skyinu.vm_eraser.MemoryFileParser$parse$1",
      "label": "com.skyinu.vm_eraser.MemoryFileParser$parse$1",
      "color": "#ff0",
      "size": 3,
      "x": 460,
      "y": 500,
      "debug_level": 2,
      "debug_angel": 180.0
    },
    {
      "id": "com.skyinu.vm_eraser.MapItemModel_$$ExternalSynthetic0",
      "label": "com.skyinu.vm_eraser.MapItemModel_$$ExternalSynthetic0",
      "color": "#ff0",
      "size": 3,
      "x": 540,
      "y": 500,
      "debug_level": 2,
      "debug_angel": 0.0
    }
  ],
  "edges": [
    {
      "id": "com.skyinu.vm_eraser.VMErasercom.skyinu.vm_eraser.MemoryFileParser",
      "label": "com.skyinu.vm_eraser.VMErasercom.skyinu.vm_eraser.MemoryFileParser",
      "source": "com.skyinu.vm_eraser.VMEraser",
      "target": "com.skyinu.vm_eraser.MemoryFileParser",
      "color": "#86fde8"
    },
    {
      "id": "com.skyinu.vm_eraser.VMErasercom.skyinu.vm_eraser.MapItemModel",
      "label": "com.skyinu.vm_eraser.VMErasercom.skyinu.vm_eraser.MapItemModel",
      "source": "com.skyinu.vm_eraser.VMEraser",
      "target": "com.skyinu.vm_eraser.MapItemModel",
      "color": "#86fde8"
    },
    {
      "id": "com.skyinu.vm_eraser.MemoryFileParsercom.skyinu.vm_eraser.MapItemModel",
      "label": "com.skyinu.vm_eraser.MemoryFileParsercom.skyinu.vm_eraser.MapItemModel",
      "source": "com.skyinu.vm_eraser.MemoryFileParser",
      "target": "com.skyinu.vm_eraser.MapItemModel",
      "color": "#f5af19"
    },
    {
      "id": "com.skyinu.vm_eraser.MemoryFileParsercom.skyinu.vm_eraser.MemoryFileParser$parse$1",
      "label": "com.skyinu.vm_eraser.MemoryFileParsercom.skyinu.vm_eraser.MemoryFileParser$parse$1",
      "source": "com.skyinu.vm_eraser.MemoryFileParser",
      "target": "com.skyinu.vm_eraser.MemoryFileParser$parse$1",
      "color": "#f5b019"
    },
    {
      "id": "com.skyinu.vm_eraser.MapItemModelcom.skyinu.vm_eraser.MapItemModel_$$ExternalSynthetic0",
      "label": "com.skyinu.vm_eraser.MapItemModelcom.skyinu.vm_eraser.MapItemModel_$$ExternalSynthetic0",
      "source": "com.skyinu.vm_eraser.MapItemModel",
      "target": "com.skyinu.vm_eraser.MapItemModel_$$ExternalSynthetic0",
      "color": "#f5af19"
    },
    {
      "id": "com.skyinu.vm_eraser.MemoryFileParser$parse$1com.skyinu.vm_eraser.MapItemModel",
      "label": "com.skyinu.vm_eraser.MemoryFileParser$parse$1com.skyinu.vm_eraser.MapItemModel",
      "source": "com.skyinu.vm_eraser.MemoryFileParser$parse$1",
      "target": "com.skyinu.vm_eraser.MapItemModel",
      "color": "#a8e063"
    },
    {
      "id": "com.skyinu.vm_eraser.MemoryFileParser$parse$1com.skyinu.vm_eraser.MemoryFileParser",
      "label": "com.skyinu.vm_eraser.MemoryFileParser$parse$1com.skyinu.vm_eraser.MemoryFileParser",
      "source": "com.skyinu.vm_eraser.MemoryFileParser$parse$1",
      "target": "com.skyinu.vm_eraser.MemoryFileParser",
      "color": "#a8e063"
    }
  ]
}