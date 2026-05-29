```mermaid
classDiagram
direction TB

%% === game package ===
class Player {
+String name
+Entity currentEntity
+MutableList inventory
+Boolean playsAsHuman
}
class Spinner {
<<object>>
+getValue(Int? seed) SpinnerValue
}
class SpinnerValue {
<<enumeration>>
ESCAPE
FANGS
BLADES
AIM
+Int number
}

%% === game.cards ===
class Card {
<<abstract>>
+String cardNameKey
+String cardDescriptionKey
-Boolean privateIsFaceDown
+Boolean isFaceDown
+flip()
}
class Entity {
+Boolean isHuman
+List canDamagedBy
+Int healthPoints
+List abilities
+Int plusSpeed
}
class Item {
+ItemType itemType
}
class DamageType {
<<enumeration>>
KNIFE
GUN
GRENADE
GRENADE_LAUNCHER
FANGS
}
class EntityAbility {
<<enumeration>>
DOUBLE_HEAL
START_GUN
START_KNIFE
}
class ItemType {
<<enumeration>>
FIRST_AID_KIT
WIN_CONDITION
KNIFE
GUN
GRENADE
GRENADE_LAUNCHER
+String nameKey
+String descriptionKey
+DamageType? damageType
+List? spinnerValuesToUse
+List? statesToUse
+Boolean isSingleUsable
}

%% === game.board ===
class Board {
-List cellList
-List wallList
+cellExists(Pair) Boolean
-getExistCell(Pair) Cell
+getCellWithCardCoordinates(Card) Pair?
+cellHasCondition(Pair, CellType) Boolean
+wallExists(Pair, Direction) Boolean
+existCellHasNeighbour(Pair, Direction) Boolean
-getExistCellExistNeighbour(Pair, Direction) Cell
+isCardNeighbour(Pair, Card) Boolean
+existCellHasCard(Pair, Card) Boolean
+existCellAddCard(Pair, Card)
+existCellRemoveCard(Pair, Card)
+existCellHasAnyCard(Pair, List) Boolean
+existCellAddCards(Pair, List)
+move(Card, Direction)
+hasOpponents(Pair, Boolean) Boolean
+getHighestOpponent(Pair, Boolean) Entity
+hasItems(Pair) Boolean
+takeHighestItem(Pair) Item
+hasFaceDown(Pair) Boolean
+flipFaceDown(Pair)
+isEmpty(Pair) Boolean
+Int width
+Int height
+List cellsCardsAndConditions
+List walls
+List cardsOnBoard
+List monstersOnBoard
+List humansOnBoard
+List onFinishHumans
}
class Cell {
+List conditions
+MutableList cards
}
class CellType {
<<enumeration>>
START
FINISH
}
class Direction {
<<enumeration>>
UP
DOWN
RIGHT
LEFT
+Pair delta
+String nameKey
}

%% === game.actions ===
class Action {
<<abstract>>
+List nameKey
+List descriptionKey
-GameContext context
+Boolean isAvailable
+execute() Boolean
+getName() String
+getDescription() String
}
class ActionManager {
-GameContext context
-List automaticActions
-List nonautomaticActions
+Action? higherAvailableAutomatic
+List availableNonautomaticList
}
class ChooseMonsterAction {
-GameContext context
-Entity monster
+Boolean isAvailable
+execute() Boolean
}
class EndBattleAction {
+GameContext context
+Boolean isAvailable
+execute() Boolean
}
class EndGameAction {
-GameContext context
+Boolean isAvailable
+execute() Boolean
}
class FinishTurnAction {
+GameContext context
+Boolean isAvailable
+execute() Boolean
}
class FlipCardAction {
-GameContext context
+Boolean isAvailable
+execute() Boolean
}
class GiveItemToOtherPlayerAction {
-GameContext context
-Entity otherPlayerHuman
-ItemType itemType
+Boolean isAvailable
+execute() Boolean
}
class MonsterPlayerTurnSkipAction {
-GameContext context
+Boolean isAvailable
+execute() Boolean
}
class MoveAction {
-GameContext context
-Direction direction
+Boolean isAvailable
+execute() Boolean
}
class PickUpAction {
+GameContext context
+Boolean isAvailable
+execute() Boolean
}
class SkipAttackAction {
-GameContext context
+Boolean isAvailable
+execute() Boolean
}
class SpinnerAction {
-GameContext context
+Boolean isAvailable
+execute() Boolean
}
class SpinnerSpinnedAction {
-GameContext context
+Boolean isAvailable
+execute() Boolean
}
class StartBattleAction {
-GameContext context
+Boolean isAvailable
+execute() Boolean
}
class UseFirstAidKitAction {
-GameContext context
+Boolean isAvailable
+execute() Boolean
}
class UseWeaponAction {
+GameContext context
+ItemType itemType
+Boolean isAvailable
+execute() Boolean
}
class UseWinConditionAction {
+GameContext context
+Boolean isAvailable
+execute() Boolean
}

%% === game.factories ===
class BoardFactory {
-getDefaultCellList() List
-getDefaultWallList() List
-fillCellListWithDefaultCards(List, List, Int?)
-fillCellListWithPlayerHumans(List, List, Int?)
+createDefaultBoard(List, List, Int?) Board
}
class DeckFactory {
+createDefaultCardDeck() List
}
class HumanFactory {
+createDefaultShuffledHumans(Int, Int, Int?) List
}
class PlayerFactory {
+createPlayers(List, List) List
}

%% === game.gameControl ===
class GameConfig {
<<data>>
+Int playerCount
+List playerNames
+Int startHealth
+String localization
}
class GameInitialization {
+Int playerCount
-MutableList playerNames
+List playerNamesList
+Int startHealth
+setPlayerCount(Int) Int
+setPlayerName(Int, String) String
+setPlayerStartHealth(Int) Int
+buildGameConfig() GameConfig
}
class GameStarter {
+start(GameConfig, Int?) Pair
}
class GameState {
<<enumeration>>
MONSTER_CHOOSING
STEP_SPINNER_SPINNING
STEP_SPINNER_SPINNED
STEPPING
BATTLE_SPINNER_SPINNING
BATTLE_SPINNER_SPINNED
BATTLE
ESCAPE_SPINNER_SPINNING
ESCAPE_SPINNER_SPINNED
ESCAPE
TURN_FINISH
}
class GameContext {
-Board board
+String localization
+Int winConditionsToWin
-Int? seed
+Int playersSize
-TurnManager turnManager
-PlayersManager playersManager
-WinManager winManager
-InventoryManager inventoryManager
-MovementManager movementManager
-HealthManager healthManager
-BattleManager battleManager
-SpinnerManager spinnerManager
+GameState gameState
+SpinnerValue spinnerValue
+spin(Int?)
+Int activePlayerIndex
+nextTurn()
+incrementWinConditionsCount()
+Int winConditions
+Boolean isWin
+Boolean isLose
+nonactivePlayerHasEntity(Entity) Boolean
+playerWithEntityAddItem(Entity, Item)
+List playersNames
+List playersOnFinishNames
-Player activePlayer
+String activePlayerName
+Boolean activePlayerPlaysAsHuman
+List activePlayerInventoryList
+Set activePlayerInventoryTypesSet
-Entity activePlayerEntity
+String activePlayerEntityNameKey
+Int activePlayerHealth
+Boolean activePlayerEntityIsAlive
+activePlayerHasAbility(EntityAbility) Boolean
+activePlayerSetEntity(Entity)
+activePlayerAddHealth(Int)
+Pair activePlayerCoordinates
+Int activePlayerStepCount
+activePlayerStartStepping()
+activePlayerMove(Direction)
+activePlayerHasItem(ItemType) Boolean
+activePlayerRemoveItem(ItemType)
+getActivePlayerItemWithType(ItemType) Item
+Boolean activePlayerHasOpponent
+Boolean isStateForStartBattle
+startBattle()
+Boolean monsterIsAlive
+monsterAddHealth(Int)
+monsterCanDamagedBy(DamageType) Boolean
+Boolean isEndBattle
+goToBattleSpinner()
+goInBattleState()
+endBattle()
+Boolean activePlayerOnItem
+activePlayerPickUpItem()
+Boolean activePlayerOnFaceDownCard
+flip()
+Int boardWidth
+Int boardHeight
+List boardCellsCardsAndConditions
+List boardWalls
+List monstersOnBoard
+List nonactiveHumansOnBoard
+wallExists(Pair, Direction) Boolean
+cellExists(Pair) Boolean
+cellHasCard(Pair, Card) Boolean
+cellAddCard(Pair, Card)
+cellHasCondition(Pair, CellType) Boolean
+isEntityNeighbourOfActive(Pair, Entity) Boolean
}
class ReadOnlyGameContext {
-GameContext context
+GameState gameState
+String localization
+List playersNames
+List playersOnFinishNames
+Boolean isWin
+Boolean isLose
+Int activePlayerIndex
+String activePlayerName
+String activePlayerEntityNameKey
+List activePlayerInventory
+Int activePlayerHealth
+Int activePlayerStepCount
+Pair? activePlayerCoordinates
+Int winConditionsToWin
+Int winConditions
+SpinnerValue spinnerValue
+Int boardWidth
+Int boardHeight
+List boardCellsCardsAndConditions
+List boardWalls
}

%% === game.gameControl.contextManagers ===
class BattleManager {
+TurnManager turnManager
+Board board
+PlayersManager playersManager
+MovementManager movementManager
+HealthManager healthManager
-Player activePlayer
-Entity activePlayerEntity
-Pair activePlayerCoordinates
+Boolean activePlayerHasOpponent
+Entity? monster
+Int? tempIndex
+Boolean isActive
+startBattle()
+Boolean monsterIsAlive
+monsterAddHealth(Int)
+monsterCanDamagedBy(DamageType) Boolean
+isEndBattle(SpinnerValue) Boolean
+endBattle()
+switchToTempIndex()
}
class HealthManager {
+PlayersManager playersManager
+InventoryManager inventoryManager
+Board board
+addHealth(Entity, Int)
}
class InventoryManager {
+hasItem(Player, ItemType) Boolean
+getPlayerItemWithType(Player, ItemType) Item
+addItem(Player, Item)
+removeItem(Player, ItemType)
+getInventoryItems(Player) List
+getInventoryItemTypes(Player) List
}
class MovementManager {
+TurnManager turnManager
+Board board
-Entity entity
+Pair currentCoordinates
+Int currentStepCount
+Boolean hasSteps
+move(Direction)
+startStepping(Int)
+stopStepping()
}
class PlayersManager {
+List players
+getPlayerWithEntity(Entity) Player?
+getPlayerWithEntityIndex(Entity) Int
}
class SpinnerManager {
+SpinnerValue spinnerValue
+spin(Int?)
+resetForBattle()
}
class TurnManager {
+List players
+Int activePlayerIndex
+Player activePlayer
+nextTurn()
}
class WinManager {
+Int winConditionsToWin
+Int winConditions
+incrementWinConditions()
+isWin() Boolean
+isLose(List) Boolean
}

%% === gui state and models ===
class InitUiState {
<<data>>
+Int playerCount
+List playerNames
+Int startHealth
+List statistics
+String? errorMessage
}
class PlayerStatisticsUiModel {
<<data>>
+String name
+Int wins
}
class GameUiState {
<<data>>
+String activePlayerName
+String activePlayerEntityName
+Int activePlayerHealth
+Int activePlayerStepCount
+Pair? activePlayerCoordinates
+List players
+FinalGameUiModel? finalGame
+GameState gameState
+String gameStateName
+String spinnerValue
+Int winConditions
+Int winConditionsToWin
+List inventory
+List boardRows
+GameBoardCellUiModel? selectedCell
+List actions
+String? automaticActionName
+String? message
}
class GamePlayerUiModel {
<<data>>
+String name
+Boolean isActive
}
class FinalGameUiModel {
<<data>>
+Boolean isWin
+List winnerNames
}
class GameActionUiModel {
<<data>>
+Int id
+String name
+String description
}
class GameCardUiModel {
<<data>>
+String name
+String description
+List details
+Boolean isFaceDown
}
class GameBoardCellUiModel {
<<data>>
+Int x
+Int y
+String shortText
+String? topCardText
+List cards
+List conditions
+List walls
+Boolean hasTopWall
+Boolean hasRightWall
+Boolean hasBottomWall
+Boolean hasLeftWall
+Boolean isActivePlayerCell
+Boolean isSelected
}

%% === gui view models and repositories ===
class InitViewModel {
-PlayerStatisticsRepository statisticsRepository
-GameInitialization gameInit
-MutableStateFlow _uiState
+StateFlow uiState
-MutableSharedFlow _navigationEvents
+SharedFlow navigationEvents
+changePlayerCount(Int)
+changePlayerName(Int, String)
+changeStartHealth(Int)
+onStartGameClicked()
-updateUiState()
}
class GameViewModel {
-ActionManager manager
-ReadOnlyGameContext context
-List availableActions
-Pair? selectedCellCoordinates
-ResourceBundle? localizationBundle
-MutableStateFlow _uiState
+StateFlow uiState
+onActionClicked(Int)
+onCellClicked(Int, Int)
+refresh()
-applyAutomaticActions(String?)
-updateUiState(String?, String?)
-buildUiState(String?, String?) GameUiState
-buildPlayers() List
-buildFinalGame() FinalGameUiModel?
-buildBoardRows() List
-cellToUiModel(Int, Int, Pair, Boolean, Boolean) GameBoardCellUiModel
-buildWallDirections(Pair) List
-matches(Pair, Direction) Boolean
-wallDirectionToText(Direction) String
-buildCellShortText(Pair, Boolean) String
-toCellLabel(Card) String
-toCellLabel(Entity) String
-toCellLabel(ItemType) String
-cardToUiModel(Card) GameCardUiModel
-cardDetails(Card) List
-cellConditionToText(CellType) String
-localizeEnum(String, String) String
-localize(String) String
}
class PlayerStatisticsRepository {
-Path statisticsFile
+loadStatistics() List
+recordFinalGame(FinalGameUiModel)
-incrementPlayerWins(String)
-loadProperties() Properties
-saveProperties(Properties)
-getCount(String) Int
-toStatisticsId(String) String
}

%% === gui top-level files ===
class MainKt {
<<file>>
+main()
+appNavigation(Function0)
}
class InitScreenKt {
<<file>>
+initScreen(InitViewModel)
-initContent(InitUiState, InitViewModel)
-settingsForm(InitUiState, InitViewModel)
-statisticsSection(List)
-statisticsHeader()
-statisticsRow(PlayerStatisticsUiModel)
-playerCountSelector(Int, Function1)
-startHealthInput(Int, Function1)
-playerNamesHeader()
-playerNameInput(Int, String, Function2)
-errorText(String)
-startGameButton(Function0)
}
class GameScreenKt {
<<file>>
+gameScreen(GameViewModel, Function1)
-gameContent(GameUiState, GameViewModel)
-leftPanel(GameUiState, GameViewModel)
-rightPanel(GameUiState, Function2)
-gameStatusCard(GameUiState)
-gameProgressCard(GameUiState)
-playersCard(List)
-playerRow(GamePlayerUiModel)
-boardCard(GameUiState, Function2, Modifier)
-boardCell(GameBoardCellUiModel, Function2)
-boardCellContent(GameBoardCellUiModel)
-wallOverlay(GameBoardCellUiModel)
-wallLine(Modifier)
-selectedCellCard(GameBoardCellUiModel?)
-selectedCellContent(GameBoardCellUiModel)
-cardInfo(GameCardUiModel)
-inventoryCard(List)
-messageCard(GameUiState)
-actionsCard(List, Function1)
-actionButton(GameActionUiModel, Function0)
-refreshButton(Function0)
-infoRow(String, String)
-backgroundColor(GameBoardCellUiModel) Color
-coordinatesText(GameBoardCellUiModel) String
-formatCoordinates(Pair) String
}
class FinalScreenKt {
<<file>>
+finalScreen(FinalGameUiModel, Function0)
-finalGameContent(FinalGameUiModel)
}
class PlayerStatisticsRepositoryKt {
<<file>>
-defaultStatisticsFile() Path
}

%% === Inheritance ===
Card <|-- Entity
Card <|-- Item
Action <|-- ChooseMonsterAction
Action <|-- EndBattleAction
Action <|-- EndGameAction
Action <|-- FinishTurnAction
Action <|-- FlipCardAction
Action <|-- GiveItemToOtherPlayerAction
Action <|-- MonsterPlayerTurnSkipAction
Action <|-- MoveAction
Action <|-- PickUpAction
Action <|-- SkipAttackAction
Action <|-- SpinnerAction
Action <|-- SpinnerSpinnedAction
Action <|-- StartBattleAction
Action <|-- UseFirstAidKitAction
Action <|-- UseWeaponAction
Action <|-- UseWinConditionAction

%% === Composition ===
Player *-- Item : inventory
Board *-- Cell : cellList
GameContext *-- TurnManager
GameContext *-- PlayersManager
GameContext *-- WinManager
GameContext *-- InventoryManager
GameContext *-- MovementManager
GameContext *-- HealthManager
GameContext *-- BattleManager
GameContext *-- SpinnerManager
ActionManager *-- Action : automaticActions
ActionManager *-- Action : nonautomaticActions
InitViewModel *-- GameInitialization
InitViewModel *-- PlayerStatisticsRepository
GameViewModel *-- ActionManager
GameViewModel *-- ReadOnlyGameContext

%% === Aggregation ===
Cell o-- Card : cards
Cell o-- CellType : conditions
InitUiState o-- PlayerStatisticsUiModel : statistics
GameUiState o-- GamePlayerUiModel : players
GameUiState o-- FinalGameUiModel : finalGame
GameUiState o-- GameCardUiModel : inventory
GameUiState o-- GameBoardCellUiModel : boardRows
GameUiState o-- GameBoardCellUiModel : selectedCell
GameUiState o-- GameActionUiModel : actions
GameBoardCellUiModel o-- GameCardUiModel : cards

%% === Association ===
Player --> Entity : currentEntity
Entity --> DamageType : canDamagedBy
Entity --> EntityAbility : abilities
Item --> ItemType : itemType
ItemType --> DamageType : damageType
ItemType --> SpinnerValue : spinnerValuesToUse
ItemType --> GameState : statesToUse
ReadOnlyGameContext --> GameContext : context
PlayerStatisticsRepository --> PlayerStatisticsUiModel

%% === Dependencies ===
Spinner ..> SpinnerValue
GameContext --> Board : board
GameContext --> GameState : gameState
GameContext --> Player : activePlayer
GameContext --> Entity : activePlayerEntity
GameContext --> Direction
GameContext --> Card
GameContext --> CellType
GameContext --> DamageType
GameContext --> EntityAbility
GameContext --> Item
GameContext --> ItemType
ReadOnlyGameContext ..> GameState
ReadOnlyGameContext ..> SpinnerValue
ReadOnlyGameContext ..> Item
ReadOnlyGameContext ..> Card
ReadOnlyGameContext ..> CellType
Action --> GameContext : context
Action ..> ResourceBundle
ActionManager --> GameContext : context
ActionManager ..> Direction
ActionManager ..> ItemType
ChooseMonsterAction --> Entity : monster
GiveItemToOtherPlayerAction --> Entity : otherPlayerHuman
GiveItemToOtherPlayerAction --> ItemType : itemType
MoveAction --> Direction : direction
UseWeaponAction --> ItemType : itemType
UseWinConditionAction ..> ItemType
UseWinConditionAction ..> CellType
UseFirstAidKitAction ..> ItemType
UseFirstAidKitAction ..> EntityAbility

BattleManager --> TurnManager
BattleManager --> Board
BattleManager --> PlayersManager
BattleManager --> MovementManager
BattleManager --> HealthManager
BattleManager --> Player
BattleManager --> Entity
BattleManager ..> SpinnerValue
BattleManager ..> DamageType
HealthManager --> PlayersManager
HealthManager --> InventoryManager
HealthManager --> Board
HealthManager --> Entity
InventoryManager --> Player
InventoryManager --> Item
InventoryManager --> ItemType
MovementManager --> TurnManager
MovementManager --> Board
MovementManager --> Direction
MovementManager --> Entity
PlayersManager --> Player
PlayersManager --> Entity
SpinnerManager ..> Spinner
SpinnerManager ..> SpinnerValue
TurnManager --> Player
WinManager --> Player

Board ..> Card
Board ..> Entity
Board ..> Item
Board ..> Direction
Board ..> CellType

BoardFactory ..> Board
BoardFactory ..> Cell
BoardFactory ..> CellType
BoardFactory ..> Card
BoardFactory ..> Entity
DeckFactory ..> Card
DeckFactory ..> Entity
DeckFactory ..> Item
DeckFactory ..> ItemType
DeckFactory ..> DamageType
HumanFactory ..> Entity
HumanFactory ..> DamageType
HumanFactory ..> EntityAbility
PlayerFactory ..> Player
PlayerFactory ..> Entity
PlayerFactory ..> Item
PlayerFactory ..> ItemType
PlayerFactory ..> EntityAbility

GameInitialization ..> GameConfig
GameStarter ..> GameConfig
GameStarter ..> ActionManager
GameStarter ..> ReadOnlyGameContext
GameStarter ..> HumanFactory
GameStarter ..> PlayerFactory
GameStarter ..> DeckFactory
GameStarter ..> BoardFactory
GameStarter ..> GameContext

InitViewModel ..> InitUiState
InitViewModel ..> GameStarter
InitViewModel ..> ActionManager
InitViewModel ..> ReadOnlyGameContext
GameViewModel ..> GameUiState
GameViewModel ..> GamePlayerUiModel
GameViewModel ..> FinalGameUiModel
GameViewModel ..> GameActionUiModel
GameViewModel ..> GameCardUiModel
GameViewModel ..> GameBoardCellUiModel
GameViewModel ..> Action
GameViewModel ..> Card
GameViewModel ..> Entity
GameViewModel ..> Item
GameViewModel ..> ItemType
GameViewModel ..> CellType
GameViewModel ..> Direction
GameViewModel ..> GameState
GameViewModel ..> SpinnerValue
GameViewModel ..> ResourceBundle
PlayerStatisticsRepository ..> FinalGameUiModel
PlayerStatisticsRepository ..> Path
PlayerStatisticsRepository ..> Properties

MainKt ..> InitViewModel
MainKt ..> GameViewModel
MainKt ..> PlayerStatisticsRepository
MainKt ..> ActionManager
MainKt ..> ReadOnlyGameContext
MainKt ..> FinalGameUiModel
MainKt ..> InitScreenKt
MainKt ..> GameScreenKt
MainKt ..> FinalScreenKt
InitScreenKt ..> InitViewModel
InitScreenKt ..> InitUiState
InitScreenKt ..> PlayerStatisticsUiModel
GameScreenKt ..> GameViewModel
GameScreenKt ..> GameUiState
GameScreenKt ..> FinalGameUiModel
GameScreenKt ..> GamePlayerUiModel
GameScreenKt ..> GameActionUiModel
GameScreenKt ..> GameCardUiModel
GameScreenKt ..> GameBoardCellUiModel
FinalScreenKt ..> FinalGameUiModel
PlayerStatisticsRepositoryKt ..> PlayerStatisticsRepository
```
