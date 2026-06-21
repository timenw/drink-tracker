#!/usr/bin/env python3
"""
Generate Chinese baijiu culture app icon.
Design: Deep red background + golden/white traditional wine vessel silhouette + Chinese aesthetic.
"""
import struct
import zlib
import math

def create_png(width, height, color_fn):
    raw = b''
    for y in range(height):
        raw += b'\x00'
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

def baijiu_icon(x, y, w, h):
    """Chinese baijiu bottle icon on deep red background with golden accents."""
    cx, cy = w // 2, h // 2
    
    # Background: deep Chinese red with subtle gradient
    dist_from_center = math.sqrt((x - cx)**2 + (y - cy)**2)
    max_dist = math.sqrt(cx**2 + cy**2)
    
    # Rounded square background
    border = 4
    if x < border or x >= w - border or y < border or y >= h - border:
        # Rounded corners - transparent
        corner_dist = min(
            math.sqrt((x - border)**2 + (y - border)**2),
            math.sqrt((x - (w-border))**2 + (y - border)**2),
            math.sqrt((x - border)**2 + (y - (h-border))**2),
            math.sqrt((x - (w-border))**2 + (y - (h-border))**2)
        )
        if corner_dist > border + 2:
            return (0, 0, 0, 0)
    
    # Deep red background with radial gradient
    t = dist_from_center / max_dist
    bg_r = int(180 - t * 40)   # 180 -> 140
    bg_g = int(20 - t * 10)    # 20 -> 10
    bg_b = int(25 - t * 10)    # 25 -> 15
    
    # Baijiu bottle shape (traditional Chinese wine vessel)
    # Bottle body: tall narrow rectangle with rounded top
    bottle_cx = cx
    bottle_top = int(h * 0.18)
    bottle_bottom = int(h * 0.78)
    bottle_left = int(w * 0.32)
    bottle_right = int(w * 0.68)
    neck_top = int(h * 0.18)
    neck_bottom = int(h * 0.32)
    neck_left = int(w * 0.42)
    neck_right = int(w * 0.58)
    
    # Check if point is inside bottle
    in_body = (bottle_left <= x <= bottle_right and neck_bottom <= y <= bottle_bottom)
    in_neck = (neck_left <= x <= neck_right and neck_top <= y <= neck_bottom)
    
    # Bottle shoulder (curved transition from neck to body)
    shoulder_y = neck_bottom
    shoulder_progress = (shoulder_y - y) / max(1, (bottle_bottom - neck_bottom))
    if neck_top <= y <= neck_bottom:
        shoulder_width = neck_left + (bottle_left - neck_left) * (1 - (y - neck_top) / max(1, neck_bottom - neck_top))
        shoulder_width_r = neck_right + (bottle_right - neck_right) * (1 - (y - neck_top) / max(1, neck_bottom - neck_top))
        if shoulder_width <= x <= shoulder_width_r:
            in_body = True
    
    # Golden color for bottle
    gold_r, gold_g, gold_b = 255, 215, 0
    
    if in_body or in_neck:
        # Bottle with gradient (lighter on left = highlight)
        bottle_t = (x - bottle_left) / max(1, bottle_right - bottle_left)
        highlight = max(0, 1 - abs(bottle_t - 0.3) * 2) * 0.4
        
        # Vertical gradient (lighter at top)
        if in_neck:
            v_t = (y - neck_top) / max(1, bottle_bottom - neck_top)
        else:
            v_t = (y - neck_bottom) / max(1, bottle_bottom - neck_bottom) * 0.5 + 0.3
        
        r = min(255, int(gold_r * (0.6 + highlight + v_t * 0.3)))
        g = min(255, int(gold_g * (0.5 + highlight + v_t * 0.3)))
        b = min(255, int(gold_b * (0.3 + highlight * 0.5 + v_t * 0.2)))
        
        # Bottle outline
        outline_thickness = 2
        on_left_edge = (x - bottle_left) < outline_thickness and in_body
        on_right_edge = (bottle_right - x) < outline_thickness and in_body
        on_neck_left = (x - neck_left) < outline_thickness and in_neck
        on_neck_right = (neck_right - x) < outline_thickness and in_neck
        on_bottom = (bottle_bottom - y) < outline_thickness and in_body
        
        if on_left_edge or on_right_edge or on_neck_left or on_neck_right or on_bottom:
            # Darker gold for outline
            r = int(r * 0.6)
            g = int(g * 0.6)
            b = int(b * 0.5)
        
        return (r, g, b, 255)
    
    # Red seal stamp (印章) in bottom right - traditional Chinese element
    seal_cx = int(w * 0.78)
    seal_cy = int(h * 0.82)
    seal_r = int(w * 0.12)
    seal_dist = math.sqrt((x - seal_cx)**2 + (y - seal_cy)**2)
    
    if seal_dist < seal_r:
        # Square-ish seal
        if abs(x - seal_cx) < seal_r * 0.8 and abs(y - seal_cy) < seal_r * 0.8:
            # Seal border
            border_w = 3
            if abs(x - seal_cx) > seal_r * 0.8 - border_w or abs(y - seal_cy) > seal_r * 0.8 - border_w:
                return (200, 30, 30, 255)  # Dark red border
            # Seal interior - lighter red with "酒" character hint
            seal_pattern = (x - seal_cx + y - seal_cy) % 8
            if seal_pattern < 2:
                return (220, 50, 50, 255)
            return (180, 25, 25, 255)
    
    # Decorative horizontal line (golden) - like a label band
    label_y1 = int(h * 0.48)
    label_y2 = int(h * 0.52)
    if label_y1 <= y <= label_y2 and bottle_left + 4 <= x <= bottle_right - 4:
        return (255, 220, 50, 200)
    
    # Small golden circle (like a Chinese coin / 铜钱) top left
    coin_cx = int(w * 0.22)
    coin_cy = int(h * 0.22)
    coin_r = int(w * 0.08)
    coin_dist = math.sqrt((x - coin_cx)**2 + (y - coin_cy)**2)
    if coin_dist < coin_r:
        if coin_dist > coin_r - 2:
            return (255, 215, 0, 255)  # Gold ring
        # Square hole in center
        hole = int(coin_r * 0.35)
        if abs(x - coin_cx) < hole and abs(y - coin_cy) < hole:
            return (bg_r, bg_g, bg_b, 255)  # Transparent hole
        return (255, 200, 0, 200)
    
    return (bg_r, bg_g, bg_b, 255)


# Generate icons for all density buckets
sizes = {
    'mdpi': 48,
    'hdpi': 72,
    'xhdpi': 96,
    'xxhdpi': 144,
    'xxxhdpi': 192
}

output_dir = '/root/drink-tracker/android/app/src/main/res'
import os

for density, size in sizes.items():
    png_data = create_png(size, size, baijiu_icon)
    
    # Regular icon
    path = os.path.join(output_dir, f'mipmap-{density}', 'ic_launcher.png')
    with open(path, 'wb') as f:
        f.write(png_data)
    print(f'Created {path} ({size}x{size}, {len(png_data)} bytes)')
    
    # Round icon (same design works for round)
    path_round = os.path.join(output_dir, f'mipmap-{density}', 'ic_launcher_round.png')
    with open(path_round, 'wb') as f:
        f.write(png_data)
    print(f'Created {path_round} ({size}x{size})')

print('\nAll icons generated!')
