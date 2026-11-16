# PulseHUD — Manual verification checklist

Modules (launch vanilla-plus-Fabric-API client, open PulseHUD with Right Shift):
1. Crosshair — enabled by default; custom crosshair visible; cycle styles plus/dot/cross/circle/T; color/size/thickness/gap apply; outline toggles; dynamic spread grows while sprinting/moving; hidden while F3 open.
2. FPS — shows current FPS; toggle min/max over last 10s updates values.
3. Ping — shows own latency ms; green <80, yellow 80–150, red >150; "N/A" when disconnected; toggle bar; thresholds configurable.
4. CPS — left/right click counts shown as "L: x | R: y"; edge-detected per tick; resets each second.
5. Speed — horizontal speed from position delta, 5-sample moving average; unit BPS/km/h; decimals 0–2.
6. Keystrokes — WASD/Space/LMB/RMB grid highlights while held; layout cycle WASD+mouse/WASD only/arrows; colors apply.
7. Coordinates — X/Y/Z + facing; toggle Y; integer/one decimal.
8. Armor and Durability — four armor pieces + held item durability bars; own equipment only.
9. Potion Timers — active effects sorted by remaining time; toggle icon; filter beneficial/harmful/both.
10. Combo Counter — increments on player hits via AttackEntityCallback; resets after timeout (1–5 s); reset timeout configurable.
11. Clock — local system time; 12h/24h toggle.
12. Watermark — "PulseHUD 1.0.0"; text override; shadow toggle; opacity 0–100%; optional FPS/time suffix.
13. Target Info — crosshair on rendered entity shows name/distance/held item; max distance 1–64; toggle distance; fade-out 0–1000 ms.
14. Hit Indicator — flash crosshair/icon on hit; flash color; duration 50–500 ms; style crosshair tint/icon/both.
15. Damage Direction — DROPPED (see DROPPED.md). Confirm absent.
16. Hotbar Keys — number label above each hotbar slot; held slot highlighted; label/highlight color; toggle.
17. Item Count — total held item across inventory+hotbar; show at held slot or fixed position.
18. Theme — settings-only, no enable toggle; Preset cycle Honeycomb Amber/Midnight Blue/Mono Light/Custom; tokens update splash/settings/toasts/HUD immediately; editing custom field switches to Custom.
19. Memory — "MEM used/max GB"; refreshes once per second; toggle max; warn color >85%.
20. Compass — heading bar N/E/S/W + degree ticks from yaw; width 100–400 px; toggle degrees.
21. Looking At Block — block name under crosshair; tool hint when exposed; max distance 1–16; toggle tool hint.
22. Light Level — block light + sky light at feet; toggle sky light.
23. Session Timer — HH:MM:SS since join; resets on reconnect; format toggle minutes only.
24. Movement State — SPRINTING/SNEAKING/SWIMMING/FLYING/WALKING from own flags; toggle icon.
25. Biome — localized biome name at position; toggle; position corner.

Controls keybind — "PulseHUD" default Right Shift in Options > Controls > PulseHUD category; rebindable; persists.
Splash — first open shows honeycomb; center hexagon/Enter opens settings; Escape closes menu.
Theme switch — preset change repaints splash/settings/toasts/HUD immediately without restart.
Reduce motion — default off; toggling disables animations (splash fade/scale, menu fade/slide/toggle).
Layout editor — "Edit HUD Layout" drags enabled modules; snap 5 px; anchor+offset survives resize; Escape cancels, Enter saves.
Close prompt — Escape/outside with unsaved changes shows Save/Discard/Cancel; Cancel keeps menu open.
Config reload — `/pulsehud reload` reloads file; success/failure toast. `/pulsehud reset` resets all modules after confirmation.
