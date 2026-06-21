#!/usr/bin/env python3
"""Create a simple notification icon PNG"""
import struct
import zlib

def create_png(width, height, color_fn):
    """Create a PNG file with given color function"""
    raw = b''
    for y in range(height):
        raw += b'\x00'  # filter byte
        for x in range(width):
            r, g, b, a = color_fn(x, y, width, height)
            raw += bytes([r, g, b, a])
    
    def chunk(ctype, data):
        c = ctype + data
        return struct.pack('>I', len(data)) + c + struct.pack('>I', zlib.crc32(c) & 0xffffffff)
    
    png = b'\x89PNG\r\n\x1a\n'
    png += chunk(b'IHDR', struct.pack('>IIBBBBB', width, height, 8, 6, 0, 0, 0))
    png += chunk(b'IDAT', zlib.compress(raw))
    png += chunk(b'IEND', b'')
    return png

# 48x48 orange circle notification icon
def notif_icon(x, y, w, h):
    cx, cy = w//2, h//2
    dx, dy = x - cx, y - cy
    dist = (dx*dx + dy*dy) ** 0.5
    if dist < 18:
        return (255, 140, 0, 255)  # orange
    elif dist < 20:
        alpha = max(0, min(255, int((20 - dist) * 127)))
        return (255, 140, 0, alpha)
    return (0, 0, 0, 0)

png_data = create_png(48, 48, notif_icon)
with open('/root/drink-tracker/android/app/src/main/res/drawable/ic_notification.png', 'wb') as f:
    f.write(png_data)
print(f'Created ic_notification.png ({len(png_data)} bytes)')
