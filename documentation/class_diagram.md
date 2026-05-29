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
+start(GameConfig, Int?) ActionManager
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
+Boolean isWin
+Boolean isLose
+nonactivePlayerHasEntity(Entity) Boolean
+playerWithEntityAddItem(Entity, Item)
+List monstersOnBoard
+List nonactiveHumansOnBoard
+List playersNames
-Player activePlayer
+String activePlayerName
+Boolean activePlayerPlaysAsHuman
+Set activePlayerInventoryTypesSet
-Entity activePlayerEntity
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
+wallExists(Pair, Direction) Boolean
+cellExists(Pair) Boolean
+cellHasCard(Pair, Card)
+cellAddCard(Pair, Card)
+cellHasCondition(Pair, CellType) Boolean
+isEntityNeighbourOfActive(Pair, Entity) Boolean
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

%% === Aggregation ===
Cell o-- Card : cards
Cell o-- CellType : conditions

%% === Association ===
Player --> Entity : currentEntity
Entity --> DamageType : canDamagedBy
Entity --> EntityAbility : abilities
Item --> ItemType : itemType
ItemType --> DamageType : damageType
ItemType --> SpinnerValue : spinnerValuesToUse
ItemType --> GameState : statesToUse

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
Action --> GameContext : context
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
GameStarter ..> HumanFactory
GameStarter ..> PlayerFactory
GameStarter ..> DeckFactory
GameStarter ..> BoardFactory
GameStarter ..> GameContext
