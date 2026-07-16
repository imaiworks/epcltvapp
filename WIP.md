# WIP: upstream (daig0rian/epcltvapp) の新機能を取り込む

## 目的
upstream/master に以下2つの新機能が追加されているのを発見したため、フォークに取り込む。
チャンネルロゴ表示を自前実装しようとしていたが、upstreamで既に実装済みだったので
そちらを取り込む方針に変更。

取り込む upstream コミット:
- `cf9f2c2` 番組名を1分ごとに自動更新
- `3fdfedc` ライブ視聴カードにチャンネルロゴを表示 (#35)(#38)

## 注意点
このフォークは `MainFragment.kt` / `ChannelItem.kt` / `OriginalCardPresenter.kt` に
キーワードグループ機能などの独自変更が入っているため、`git merge upstream/master` で
コンフリクトが発生する可能性が高い。

## 完了済み
- (未着手)

## 残タスク
1. `git merge upstream/master` 実行
2. コンフリクト解消（発生した場合）
3. ビルド確認 (`assembleDebug`)
4. ユーザーによる実機動作確認（番組名自動更新・チャンネルロゴ表示）
5. PR作成（origin: imaiworks/epcltvapp へ）
