# WIP: upstream (daig0rian/epcltvapp) の新機能を取り込む

## 目的
upstream/master に以下2つの新機能が追加されているのを発見したため、フォークに取り込む。
チャンネルロゴ表示を自前実装しようとしていたが、upstreamで既に実装済みだったので
そちらを取り込む方針に変更。

取り込む upstream コミット:
- `cf9f2c2` 番組名を1分ごとに自動更新
- `3fdfedc` ライブ視聴カードにチャンネルロゴを表示 (#35)(#38)

## upstream実装のメモ（参考）
- `GET /api/channels/{channelId}/logo`。ロゴが無いチャンネルは404。
- `ChannelItem.hasLogoData: Boolean` で事前判定し、無駄なリクエストを避けている。
- `OriginalCardPresenter` の `is ChannelItem ->` で `hasLogoData` が true の時だけ
  `authForGlide` パターンでGlide読み込み、失敗時は `mDefaultCardImage` にフォールバック。
- スケールは `centerCrop()` ではなく `fitCenter()`（ロゴは透過/白背景が多いため）。

## 注意点
このフォークは `MainFragment.kt` / `ChannelItem.kt` / `OriginalCardPresenter.kt` に
キーワードグループ機能などの独自変更が入っているため、`git merge upstream/master` で
コンフリクトが発生した（`MainFragment.kt` / `SettingsCardPresenter.kt` / `WIP.md`）。

## 完了済み
- [x] `git merge upstream/master` 実行、コンフリクト解消中

## 残タスク
1. コンフリクト解消（`MainFragment.kt` / `SettingsCardPresenter.kt`）
2. ビルド確認 (`assembleDebug`)
3. ユーザーによる実機動作確認（番組名自動更新・チャンネルロゴ表示・キーワードグループ機能に影響がないか）
4. PR作成（origin: imaiworks/epcltvapp へ）
