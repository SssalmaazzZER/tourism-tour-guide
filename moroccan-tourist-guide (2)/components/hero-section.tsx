"use client"

import { motion } from "framer-motion"
import { Search, Mic, Sparkles } from "lucide-react"
import { useState } from "react"

export function HeroSection() {
  const [searchQuery, setSearchQuery] = useState("")
  const [isListening, setIsListening] = useState(false)

  return (
    <section className="relative px-4 pt-6 pb-8">
      {/* Welcome text with animated gradient */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6 }}
        className="mb-6"
      >
        <motion.p 
          className="text-sm text-muted-foreground mb-1"
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ delay: 0.2 }}
        >
          Marhaba! Welcome to
        </motion.p>
        <motion.h1 
          className="text-4xl font-bold leading-tight"
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.3 }}
        >
          <span className="bg-gradient-to-r from-primary via-secondary to-accent bg-clip-text text-transparent">
            The Kingdom
          </span>
          <br />
          <span className="text-foreground">of Morocco</span>
        </motion.h1>
        <motion.p
          className="text-sm text-muted-foreground mt-2 max-w-[280px]"
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ delay: 0.5 }}
        >
          Discover ancient medinas, Sahara sunsets & World Cup dreams
        </motion.p>
      </motion.div>

      {/* Search bar with futuristic design */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ delay: 0.4, duration: 0.5 }}
        className="relative"
      >
        <motion.div
          className="relative flex items-center gap-3 p-2 rounded-2xl bg-muted/50 border border-border/50 backdrop-blur-sm"
          whileFocus={{ scale: 1.02 }}
          animate={{
            boxShadow: searchQuery
              ? "0 0 30px oklch(0.65 0.18 45 / 0.2)"
              : "0 0 0px oklch(0.65 0.18 45 / 0)",
          }}
        >
          <div className="flex items-center justify-center w-10 h-10 rounded-xl bg-primary/10">
            <Search className="w-5 h-5 text-primary" />
          </div>
          
          <input
            type="text"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            placeholder="Search places, food, experiences..."
            className="flex-1 bg-transparent text-sm outline-none placeholder:text-muted-foreground/70"
          />

          {/* AI assist button */}
          <motion.button
            className="flex items-center justify-center w-10 h-10 rounded-xl bg-gradient-to-br from-accent to-primary"
            whileHover={{ scale: 1.1 }}
            whileTap={{ scale: 0.9 }}
            animate={{
              boxShadow: [
                "0 0 15px oklch(0.55 0.18 265 / 0.3)",
                "0 0 25px oklch(0.55 0.18 265 / 0.5)",
                "0 0 15px oklch(0.55 0.18 265 / 0.3)",
              ],
            }}
            transition={{ duration: 2, repeat: Infinity }}
          >
            <Sparkles className="w-5 h-5 text-background" />
          </motion.button>

          {/* Voice search button */}
          <motion.button
            className={`flex items-center justify-center w-10 h-10 rounded-xl border ${
              isListening 
                ? "bg-primary border-primary" 
                : "bg-muted/50 border-border/50"
            }`}
            whileHover={{ scale: 1.1 }}
            whileTap={{ scale: 0.9 }}
            onClick={() => setIsListening(!isListening)}
            animate={isListening ? {
              scale: [1, 1.05, 1],
            } : {}}
            transition={{ duration: 0.5, repeat: isListening ? Infinity : 0 }}
          >
            <Mic className={`w-5 h-5 ${isListening ? "text-background" : "text-muted-foreground"}`} />
          </motion.button>
        </motion.div>

        {/* Quick search suggestions */}
        <motion.div
          className="flex gap-2 mt-3 overflow-x-auto pb-2 scrollbar-hide"
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ delay: 0.6 }}
        >
          {["Jemaa el-Fna", "Tagine", "Atlas Trek", "Riads", "Surfing"].map((tag, index) => (
            <motion.button
              key={tag}
              className="px-3 py-1.5 rounded-full bg-muted/30 border border-border/30 text-xs whitespace-nowrap"
              initial={{ opacity: 0, x: -10 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ delay: 0.1 * index + 0.7 }}
              whileHover={{ 
                scale: 1.05, 
                backgroundColor: "oklch(0.65 0.18 45 / 0.2)",
                borderColor: "oklch(0.65 0.18 45 / 0.5)"
              }}
              whileTap={{ scale: 0.95 }}
              onClick={() => setSearchQuery(tag)}
            >
              {tag}
            </motion.button>
          ))}
        </motion.div>
      </motion.div>
    </section>
  )
}
