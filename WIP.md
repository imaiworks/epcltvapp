# WIP: キーワードグループ「放送中」列の追加

## 目的
好きなキーワードをグループ化して登録しておくと（例:「プロ野球」= 巨人 阪神 中日 DeNA 広島カープ）、
そのキーワードにマッチする番組を放送中のチャンネルだけを一覧する列を、
「現在放送中」列のすぐ下に追加する。

計画ファイル: `C:\Users\imaiw\.claude\plans\nifty-painting-catmull.md`

## 決定事項
- 設定はテキスト入力（1行1グループ）。専用の追加/編集ダイアログは作らない。
- 書式: `グループ名: キーワード1 キーワード2 ...`（`:`/`：`区切り、キーワードは空白/`,`/`、`区切り）
- 新しい列は「現在放送中」列のすぐ下に配置する。
- キーワードは番組タイトルのみにマッチさせる。
- マッチする番組が1つもないグループも、列自体は常に表示する。
- サイドバーアイコンは新規作成せず `ic_sidebar_live` を流用。

## 完了済み
1. `KeywordGroup.kt`（新規）: パース処理
2. `preference_multiline_edittext.xml`（新規）: 複数行入力用ダイアログレイアウト
3. `preferences.xml`: 表示設定にキーワードグループ設定を追加
4. `strings.xml` / `strings.xml`(ja-rJP): 関連文言追加
5. `MainFragment.kt`:
   - `Category.KEYWORD_GROUPS` 追加（LIVE_CHANNELSの直後）
   - `addToCategory`/`reloadContentRows`/prefChangeListener 対応
   - `updateKeywordGroupRows()` / `setKeywordGroupRow()` 実装
   - `updateRows()` / `refreshLiveProgramNames()` から呼び出し
   - `IconRowHeaderPresenter` のアイコン解決フォールバック追加
6. ビルド確認 (`assembleDebug`) → BUILD SUCCESSFUL

## 残タスク
1. ユーザーによる実機/エミュレータ動作確認
   - 表示設定にキーワードグループ欄が出るか
   - 「プロ野球: 巨人 阪神」等を入力して保存→「現在放送中」列のすぐ下に列が出るか
   - マッチする番組があるチャンネルだけ並ぶか
   - マッチが0件でも列が消えないか
   - グループを減らして保存した時に余った列が消えるか
   - 「番組名更新」ボタンでグループの中身も再計算されるか
2. PR作成

## 重要な決定事項の背景
- サーバーの `getScheduleOnAir()` が返す `programs.firstOrNull()` を「現在放送中の番組」とみなす。
  既存の「番組名更新」ボタン (`refreshLiveProgramNames()`) と同じ前提。
