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

## コンフリクト解消の内容
- `MainFragment.kt`:
  - `Category` enum: `KEYWORD_GROUPS`（自分のフォーク）は残し、`PROGRAM_NAME_REFRESH`（upstreamで自動更新に置き換えられたため削除された）はupstream側を採用して削除。
  - `addToCategory` の `when` 分岐も同様（`KEYWORD_GROUPS` 残す、`PROGRAM_NAME_REFRESH` 削除）。
  - `SettingsCardPresenter.Item.Action` の `when` 分岐：`REFRESH_PROGRAM_NAMES` ケースは削除、`WAKE_ON_LAN`/`CUSTOM_URL`（フォーク独自機能）は維持。
  - companion object の定数：両方の定数（`FIXED_SETTINGS_CARD_COUNT` / `PROGRAM_NAME_AUTO_REFRESH_INTERVAL_MS`）を維持。
  - `refreshLiveProgramNames()` 本体・`startProgramNameAutoRefresh()`/`stopProgramNameAutoRefresh()`・チャンネルロゴ表示・`updateKeywordGroupRows()` はいずれも自動マージで無事共存できた（コンフリクトなし）。
- `SettingsCardPresenter.kt`: `Action` enum から `REFRESH_PROGRAM_NAMES` を削除、`WAKE_ON_LAN`/`CUSTOM_URL` は維持。

## 完了済み
- [x] `git merge upstream/master` 実行、コンフリクト解消
- [x] ビルド確認 (`assembleDebug`) → BUILD SUCCESSFUL

## 残タスク
1. ユーザーによる実機動作確認
   - 番組名の1分ごと自動更新が動くか（「番組名更新」の手動ボタンは無くなった）
   - チャンネルロゴが表示されるか（ロゴが無いチャンネルはNO IMAGEのままか）
   - キーワードグループ機能（列の表示・除外キーワードなど）に影響がないか
   - Wake on LAN・カスタムURLボタンが引き続き動くか
2. PR作成（origin: imaiworks/epcltvapp へ）
