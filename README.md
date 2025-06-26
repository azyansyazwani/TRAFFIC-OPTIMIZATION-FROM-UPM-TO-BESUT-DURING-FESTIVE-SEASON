# 🚗 Traffic Optimization from UPM to Besut During Festive Season

## 📚 Course Info
**Course Code**: CNS4202  
**Course Title**: Design and Analysis of Algorithm  

## 👥 Group 4 Members
- Aya Sofea Binti Rosli 214973
- Azyan Syazwani Binti Setia 215014
- Fatin Nasuha Binti Zuraidi 215190
- Siti Najma Izzaty Binti Muhd Asyraf 216922

---

## 🎯 Project Objectives

### Main Objective
To analyze and compare two graph-based pathfinding algorithms — **Dijkstra’s Algorithm** and **A\* (A-Star) Algorithm** — to determine which provides more efficient and intelligent routing from UPM to Besut during periods of high traffic density.

### Specific Objectives
- Simulate real-world traffic scenarios using weighted graphs representing highways and junctions.
- Implement both Dijkstra and A* algorithms and compare their efficiency in terms of travel cost, time, and adaptability.
- Identify the more effective algorithm for real-time, traffic-aware routing, potentially applicable to navigation systems.
- Model traffic conditions seen during festive seasons (e.g., toll delays, congestion along LPT).

---

## 🧠 Problem Background

Malaysia faces serious traffic congestion during major festivals such as **Hari Raya** and **Chinese New Year**. One affected route is the journey from **UPM to Besut, Terengganu**, primarily via **Lebuhraya Pantai Timur (LPT)**. This project aims to develop a smart routing system to help drivers reduce travel time, cost, and stress using classical algorithmic approaches.

---

## 💻 Algorithms Used

### 1. Dijkstra’s Algorithm
- Calculates the shortest path using static weights (e.g., distance, average time).
- Explores all nodes equally, ensuring accuracy but potentially slower in large networks.

### 2. A* Algorithm
- Uses a heuristic (e.g., estimated distance to Besut) to guide the search.
- Better suited for real-time, single-destination traffic-aware routing.

---

## ⚙️ Implementation Overview

- Language: **Java**
- Graph Representation: Adjacency List with real-world traffic attributes (distance, toll, base time, congestion factor).
- Simulator adjusts edge weights during festive seasons using congestion multipliers.
- Three scenarios tested: **Time Optimization**, **Cost Optimization**, and **Balanced**.

---

## 📊 Performance Evaluation

| Criterion | Dijkstra | A* |
|----------|----------|----|
| Accuracy | ✅ Always optimal | ✅ If heuristic is admissible |
| Speed | ❌ Slower (explores all nodes) | ✅ Faster (guided by heuristic) |
| Real-time Adaptability | ❌ Limited | ✅ High |
| Scalability | ❌ Moderate | ✅ High |

### 🧪 Test Case Results
- **A\*** performed better in cost and balanced routes.
- **Dijkstra** outperformed in time-only routes due to exhaustive exploration.


