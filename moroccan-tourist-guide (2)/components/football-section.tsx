"use client"

import { motion } from "framer-motion"
import { Trophy, Flag, Users, Calendar, ChevronRight, Play } from "lucide-react"

const footballData = {
  worldCup2022: {
    achievement: "4th Place",
    matches: 7,
    goals: 11,
    highlight: "First African team to reach World Cup semi-finals"
  },
  nextMatch: {
    opponent: "Egypt",
    competition: "AFCON 2025 Qualifier",
    date: "March 2025",
    venue: "Mohammed V Stadium"
  },
  legends: [
    { name: "Achraf Hakimi", position: "RB", club: "PSG" },
    { name: "Hakim Ziyech", position: "MF", club: "Galatasaray" },
    { name: "Youssef En-Nesyri", position: "ST", club: "Sevilla" },
  ]
}

export function FootballSection() {
  return (
    <section className="px-4 py-6">
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="mb-4"
      >
        <div className="flex items-center gap-2 mb-1">
          <Trophy className="w-5 h-5 text-secondary" />
          <h2 className="text-lg font-semibold">Atlas Lions</h2>
        </div>
        <p className="text-xs text-muted-foreground">Morocco&apos;s Football Glory</p>
      </motion.div>

      {/* World Cup Achievement Card */}
      <motion.div
        initial={{ opacity: 0, scale: 0.95 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ delay: 0.2 }}
        className="relative overflow-hidden rounded-3xl bg-gradient-to-br from-emerald via-emerald/80 to-primary p-5 mb-4"
        whileHover={{ scale: 1.02 }}
      >
        {/* Decorative elements */}
        <motion.div
          className="absolute top-0 right-0 w-32 h-32 rounded-full bg-secondary/20 blur-3xl"
          animate={{ scale: [1, 1.2, 1], opacity: [0.3, 0.5, 0.3] }}
          transition={{ duration: 4, repeat: Infinity }}
        />
        <motion.div
          className="absolute bottom-0 left-0 w-24 h-24 rounded-full bg-primary/30 blur-2xl"
          animate={{ scale: [1.2, 1, 1.2] }}
          transition={{ duration: 5, repeat: Infinity }}
        />

        {/* Morocco flag colors stripe */}
        <div className="absolute top-0 left-0 w-full h-1 bg-gradient-to-r from-red-600 via-emerald to-red-600" />

        <div className="relative">
          <div className="flex items-start justify-between mb-4">
            <div>
              <motion.p 
                className="text-sm text-background/80 mb-1"
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                transition={{ delay: 0.4 }}
              >
                FIFA World Cup 2022
              </motion.p>
              <motion.h3 
                className="text-3xl font-bold text-background"
                initial={{ scale: 0.8, opacity: 0 }}
                animate={{ scale: 1, opacity: 1 }}
                transition={{ delay: 0.5, type: "spring" }}
              >
                {footballData.worldCup2022.achievement}
              </motion.h3>
            </div>
            <motion.div
              className="w-14 h-14 rounded-2xl bg-background/20 backdrop-blur-sm flex items-center justify-center"
              animate={{ rotate: [0, 5, -5, 0] }}
              transition={{ duration: 2, repeat: Infinity }}
            >
              <Flag className="w-7 h-7 text-background" />
            </motion.div>
          </div>

          <motion.p
            className="text-xs text-background/90 mb-4 leading-relaxed"
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.6 }}
          >
            {footballData.worldCup2022.highlight}
          </motion.p>

          {/* Stats */}
          <div className="flex gap-4">
            <motion.div
              className="flex items-center gap-2 px-3 py-2 rounded-xl bg-background/20"
              initial={{ opacity: 0, x: -10 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ delay: 0.7 }}
            >
              <Play className="w-4 h-4 text-background" />
              <span className="text-sm font-semibold text-background">
                {footballData.worldCup2022.matches} Matches
              </span>
            </motion.div>
            <motion.div
              className="flex items-center gap-2 px-3 py-2 rounded-xl bg-background/20"
              initial={{ opacity: 0, x: -10 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ delay: 0.8 }}
            >
              <span className="text-sm font-semibold text-background">
                ⚽ {footballData.worldCup2022.goals} Goals
              </span>
            </motion.div>
          </div>
        </div>
      </motion.div>

      {/* Next Match Card */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ delay: 0.3 }}
        className="rounded-2xl bg-muted/30 border border-border/50 p-4 mb-4"
        whileHover={{ borderColor: "oklch(0.65 0.18 45 / 0.5)" }}
      >
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-12 h-12 rounded-xl bg-gradient-to-br from-red-500 to-red-700 flex items-center justify-center">
              <span className="text-2xl">🇲🇦</span>
            </div>
            <div>
              <p className="text-xs text-muted-foreground">Next Match</p>
              <p className="font-semibold">Morocco vs {footballData.nextMatch.opponent}</p>
              <div className="flex items-center gap-2 text-xs text-muted-foreground mt-0.5">
                <Calendar className="w-3 h-3" />
                <span>{footballData.nextMatch.date}</span>
              </div>
            </div>
          </div>
          <motion.button
            className="w-10 h-10 rounded-xl bg-primary flex items-center justify-center"
            whileHover={{ scale: 1.1 }}
            whileTap={{ scale: 0.9 }}
          >
            <ChevronRight className="w-5 h-5 text-background" />
          </motion.button>
        </div>
      </motion.div>

      {/* Legends */}
      <motion.div
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ delay: 0.5 }}
      >
        <div className="flex items-center gap-2 mb-3">
          <Users className="w-4 h-4 text-accent" />
          <p className="text-sm font-medium">Star Players</p>
        </div>
        <div className="flex gap-3 overflow-x-auto pb-2 scrollbar-hide">
          {footballData.legends.map((player, index) => (
            <motion.div
              key={player.name}
              className="flex-shrink-0 flex items-center gap-3 px-4 py-3 rounded-xl bg-muted/30 border border-border/30"
              initial={{ opacity: 0, scale: 0.9 }}
              animate={{ opacity: 1, scale: 1 }}
              transition={{ delay: 0.1 * index + 0.6 }}
              whileHover={{ 
                scale: 1.05,
                borderColor: "oklch(0.65 0.18 45 / 0.5)"
              }}
            >
              <div className="w-10 h-10 rounded-full bg-gradient-to-br from-primary to-accent flex items-center justify-center text-background font-bold text-sm">
                {player.name.split(" ").map(n => n[0]).join("")}
              </div>
              <div>
                <p className="text-sm font-medium">{player.name}</p>
                <p className="text-xs text-muted-foreground">{player.position} • {player.club}</p>
              </div>
            </motion.div>
          ))}
        </div>
      </motion.div>
    </section>
  )
}
