# EPGStation の録画を見る
"EPGStation の録画を見る" は Android TV / Fire TV 向けに設計された EPGStation クライアントアプリです。

## Fork元

このリポジトリは [daig0rian/epcltvapp](https://github.com/daig0rian/epcltvapp) のフォークです。
基本機能・インストール方法・対応環境などの詳細は Fork元の README を参照してください。

## 謝辞
このアプリは私が望んでいた機能を持っているアプリでした。とても感謝しております。ありがとうございます。
感謝を込めてライブ視聴機能を追加してみました。


## このフォークで追加した機能

### ライブ視聴（現在放送中のチャンネル視聴）
録画済み番組だけでなく、現在放送中のチャンネルもそのまま視聴できます。メイン画面に「現在放送中」行が追加され、チャンネルを選ぶとすぐに再生が始まります。

- **単押し** = mpegts直送再生（低遅延。EPGStationサーバーの `config.yml` 設定次第で高画質・ほぼ即再生も可能）
- **長押し** = HLS再生（ARIB字幕対応。mpegts側が不安定なチャンネルのフォールバックにも）
- 視聴中に「REC」ボタンでその場から録画予約
- 視聴中に「ℹ️」ボタンで現在の番組情報（ジャンル・時刻・概要など）をダイアログ表示
- 「番組名更新」ボタンでチャンネルカードに現在放送中の番組名を表示

### Wake on LAN
接続設定にサーバーのMACアドレスを登録しておくと、メイン画面の設定行に「Wake on LANで起動」ボタンが表示されます。普段電源を切っているEPGStationサーバーを、アプリからそのまま起動できます。

### カスタムURLボタン
接続設定に名称とURLを登録しておくと、メイン画面の設定行にそのボタンが表示され、押すと指定URLへGETリクエストを送信します。例えば、サーバー側にシャットダウン用のURLを用意しておき、アプリから操作するといった用途を想定しています。

### その他
- 内蔵プレーヤーを libVLC から ExoPlayer (Media3) に移行
- 内蔵プレーヤー再生中にスクリーンセーバーが起動してしまう問題を修正
- USBデバッグが使えない環境でもクラッシュ内容を確認できるログ機能


## ライブ視聴の config.yml 設定について

ライブ視聴は、EPGStation の `config.yml` で `mode=0`（各配列の先頭要素）に
設定されているプロファイルを使用します。

```yaml
# epgstation/config/config.yml
stream:
    live:
        ts:
            m2ts:   # チャンネル単押し時のmpegts再生
                - ...   # mode=0
                - ...   # mode=1
            hls:    # チャンネル長押し時のHLS再生
                - ...   # mode=0
                - ...   # mode=1
```

当方は以下の設定で運用しています。

```yaml
stream:
    live:
        ts:
            m2ts:
                - name: 無変換

            hls:
                - name: 1080p
                  cmd:
                      '%FFMPEG% -re -dual_mono_mode main -i pipe:0 -sn -map 0 -threads 0 -ignore_unknown
                      -max_muxing_queue_size 1024 -f hls -hls_time 3 -hls_list_size 17 -hls_allow_cache 1
                      -hls_segment_filename %streamFileDir%/stream%streamNum%-%09d.ts -hls_flags delete_segments -c:a
                      aac -ar 48000 -b:a 192k -ac 2 -c:v libx264 -vf yadif,scale=-2:1080 -b:v 10000k -preset veryfast
                      -flags +loop-global_header %OUTPUT%'
```

`m2ts` の `無変換`（パススルー、再エンコードなし）は起動が最速・画質も最良ですが、地上デジタル1080iだと帯域を15〜18Mbps程度使うため、自宅内LANでの利用を想定しています。帯域が厳しい環境では、H.264への変換プロファイルに切り替えることをおすすめします。

## テスト環境
- J:COM セットトップボックス XA401
- EPGStation Version 2.10.0


## APKについて
このリポジトリでは APK を配布していません。各自ビルドしてご利用ください。
要望が多いようであれば、署名付きのリリースビルドを用意することも検討します。