# Angular Lifecycle Hooks: Complete Reference & Guide

A comprehensive, clear, and structured guide to understanding Angular Component Lifecycle Hooks, execution order, best practices, and practical use cases.

---

## Quick Reference Table

| Order | Phase | Hook / Method | Trigger Frequency | Primary Purpose & Key Notes |
| :---: | :--- | :--- | :--- | :--- |
| **0** | Instantiation | `constructor()` *(Class)* | **Once** (Creation) | Dependency Injection (DI) only. **Do not** put component initialization logic, API calls, or reference `@Input()` properties here. |
| **1** | Initialization | `ngOnChanges()` | **Multiple times** | Executes before `ngOnInit()` if `@Input()` properties exist, and whenever a bound `@Input()` changes. Receives a `SimpleChanges` object. |
| **2** | Initialization | `ngOnInit()` | **Once** | Main component initialization. Safe to access `@Input()` values, execute service calls, and initialize component data. |
| **3** | Detection | `ngDoCheck()` | **Every CD Cycle** | Custom change detection. Triggers on every change detection run (clicks, timers, inputs). Use for manual change tracking. |
| **4** | Projection | `ngAfterContentInit()` | **Once** | Runs after external content projected via `<ng-content>` is initialized. Safe to access `@ContentChild` / `@ContentChildren`. |
| **5** | Projection | `ngAfterContentChecked()` | **Every CD Cycle** | Runs after Angular checks projected content (`<ng-content>`) for changes. |
| **6** | View / DOM | `ngAfterViewInit()` | **Once** | Runs after the component's HTML template and child views are fully initialized. Safe to access `@ViewChild` / `@ViewChildren` and DOM elements. |
| **7** | View / DOM | `ngAfterViewChecked()` | **Every CD Cycle** | Runs after Angular checks the component's template and child views for changes. |
| **8** | Teardown | `ngOnDestroy()` | **Once** (Before Death) | Cleanup phase. Unsubscribe from RxJS Observables, detach event listeners, and clear timers (`setInterval`/`setTimeout`) to prevent memory leaks. |

---

## Visual Execution Order

```
[ Component Creation ]
          │
          ▼
    constructor()  ──── (Dependency Injection only)
          │
          ▼
   ngOnChanges()   ──── (First run, IF @Input properties exist)
          │
          ▼
      ngOnInit()   ──── (Initial data fetching & setup)
          │
          ▼
     ngDoCheck()   ──── (First change detection run)
          │
          ▼
ngAfterContentInit() ── (Projected content <ng-content> initialized)
          │
          ▼
ngAfterContentChecked() (Projected content checked)
          │
          ▼
  ngAfterViewInit()  ── (Component template & child views ready)
          │
          ▼
ngAfterViewChecked() ── (Component view checked)
          │
          ├────────┐
          │        │ (User interaction / Async event triggers Change Detection)
          │        ▼
          │   ngOnChanges()      [Only if @Input value changed]
          │        │
          │   ngDoCheck()
          │        │
          │   ngAfterContentChecked()
          │        │
          │   ngAfterViewChecked()
          │        │
          │        └───────┐
          │                │
          ▼                │
    ngOnDestroy() ◄────────┘ (Component removed from DOM)
```

---

## Detailed Breakdown & Best Practices

### 0. `constructor()` (TypeScript / Class feature)
* **What it is:** A standard ES6/TypeScript class constructor. It is **not** an Angular lifecycle hook.
* **When it runs:** Called by the JavaScript engine when Angular instantiates the component class.
* **What to do:**
  * Inject services via Dependency Injection (DI).
  * Keep it lightweight.
* **What NOT to do:**
  * Do not make HTTP requests or call service methods.
  * Do not try to read `@Input()` properties (they are `undefined` at this stage).

---

### 1. `ngOnChanges(changes: SimpleChanges)`
* **What it is:** The first actual Angular lifecycle hook executed when inputs exist.
* **When it runs:** 
  * Right before `ngOnInit()` if the component has `@Input()` properties bound.
  * Whenever an input property value changes **by reference** (e.g., passing a new primitive value or a new object reference, not mutating an existing object/array).
* **Key Arguments:** Receives a `SimpleChanges` object containing `previousValue`, `currentValue`, and `isFirstChange()`.
* **Use Case:** Reacting to dynamic changes from parent inputs (e.g., re-fetching user details when `userId` input changes).

---

### 2. `ngOnInit()`
* **What it is:** The primary initialization hook for the component.
* **When it runs:** Runs **once**, immediately after the first `ngOnChanges()` execution.
* **Use Case:**
  * Component setup logic.
  * Fetching data from APIs/services (`this.dataService.getItems()`).
  * Setting up form controls or initial state.
* **Why prefer over constructor?** All `@Input()` properties are fully evaluated and accessible by the time `ngOnInit()` runs.

---

### 3. `ngDoCheck()`
* **What it is:** Allows you to implement your own custom change detection logic.
* **When it runs:** Executes on **every single change detection cycle** (e.g., user click, promise resolution, HTTP response, mouse move).
* **Use Case:** Detecting changes that Angular's default change detection skips (such as mutating an item inside an array without changing the array reference).
* **Caution:** Called extremely frequently. Keep logic lightweight to prevent severe performance lag. Avoid using both `ngOnChanges()` and `ngDoCheck()` to monitor the same input.

---

### 4. `ngAfterContentInit()`
* **What it is:** Responds after Angular projects external content into the component via `<ng-content>`.
* **When it runs:** Runs **once**, right after the initial `ngDoCheck()`.
* **Use Case:** Safe to query and interact with projected elements using `@ContentChild` or `@ContentChildren`.

---

### 5. `ngAfterContentChecked()`
* **What it is:** Called after Angular finishes checking projected content (`<ng-content>`) for updates.
* **When it runs:** Runs once after `ngAfterContentInit()`, and then after every subsequent `ngDoCheck()`.
* **Use Case:** Performing validation or checks on projected content after Angular checks it. Keep execution short.

---

### 6. `ngAfterViewInit()`
* **What it is:** Responds after the component's view (HTML template) and all its child component views are fully rendered and mounted.
* **When it runs:** Runs **once**, after `ngAfterContentChecked()`.
* **Use Case:**
  * Accessing DOM elements directly via `ElementRef`.
  * Interacting with `@ViewChild` or `@ViewChildren` directives/components.
  * Initializing third-party JavaScript libraries (e.g., chart libraries, maps).
* **Caution:** Avoid modifying component data properties here without wrapped `setTimeout()` or asynchronous triggers, otherwise Angular will throw `ExpressionChangedAfterItHasBeenCheckedError`.

---

### 7. `ngAfterViewChecked()`
* **What it is:** Called after Angular checks the component's views and child views for changes.
* **When it runs:** Runs once after `ngAfterViewInit()`, and after every subsequent `ngAfterContentChecked()`.
* **Use Case:** Performing operations dependent on view updates (e.g., auto-scrolling a chat box to the bottom after new messages render).

---

### 8. `ngOnDestroy()`
* **What it is:** Cleanup phase called right before Angular destroys the component and removes its template from the DOM.
* **When it runs:** Runs **once**, at the end of the component lifecycle (e.g., navigating away via router, structural directive `*ngIf="false"`).
* **Use Case:**
  * Unsubscribing from RxJS Observables to prevent memory leaks.
  * Clearing timers (`clearInterval`, `clearTimeout`).
  * Detaching DOM event listeners added manually.
  * Resetting state or stopping active audio/video streams.

---

## Practical Code Skeleton

```typescript
import {
  Component,
  OnInit,
  OnChanges,
  DoCheck,
  AfterContentInit,
  AfterContentChecked,
  AfterViewInit,
  AfterViewChecked,
  OnDestroy,
  Input,
  SimpleChanges,
  ViewChild,
  ElementRef
} from '@angular/core';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-lifecycle-demo',
  template: `<p #textPara>Lifecycle Demo Content</p>`
})
export class LifecycleDemoComponent implements
  OnChanges, OnInit, DoCheck,
  AfterContentInit, AfterContentChecked,
  AfterViewInit, AfterViewChecked, OnDestroy {

  @Input() dataInput: string = '';
  @ViewChild('textPara') textPara!: ElementRef;
  
  private subscription!: Subscription;

  // 0. Constructor: DI only
  constructor() {
    console.log('0. Constructor called');
  }

  // 1. Inputs changed
  ngOnChanges(changes: SimpleChanges): void {
    console.log('1. ngOnChanges:', changes);
  }

  // 2. Component Initialization
  ngOnInit(): void {
    console.log('2. ngOnInit: Initialize data and HTTP requests');
  }

  // 3. Custom Change Detection
  ngDoCheck(): void {
    console.log('3. ngDoCheck: Change detection running');
  }

  // 4. Content (<ng-content>) Initialized
  ngAfterContentInit(): void {
    console.log('4. ngAfterContentInit: Projected content ready');
  }

  // 5. Content Checked
  ngAfterContentChecked(): void {
    console.log('5. ngAfterContentChecked: Projected content checked');
  }

  // 6. View & Child Views Initialized
  ngAfterViewInit(): void {
    console.log('6. ngAfterViewInit: DOM & ViewChild accessible', this.textPara.nativeElement);
  }

  // 7. View Checked
  ngAfterViewChecked(): void {
    console.log('7. ngAfterViewChecked: View checked');
  }

  // 8. Teardown & Cleanup
  ngOnDestroy(): void {
    console.log('8. ngOnDestroy: Cleanup subscriptions and timers');
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
```
